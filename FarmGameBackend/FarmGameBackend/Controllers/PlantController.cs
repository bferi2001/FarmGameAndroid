using FarmGameBackend.CustomExceptions;
using FarmGameBackend.Dao;
using FarmGameBackend.DbContexts;
using FarmGameBackend.Entity;
using Microsoft.AspNetCore.Mvc;
using Microsoft.EntityFrameworkCore;

namespace FarmGameBackend.Controllers
{
    [Route("api/farm/plant")]
    public class PlantController(FarmApplicationContext context) : Controller
    {
        private User CurrentUser
        {
            get
            {
                if(HttpContext.Items["CurrentUser"] == null)
                {
                    throw new BadRequestException("User is not logged in.");
                }
                return (User)HttpContext.Items["CurrentUser"]!;
            }
        }

        [HttpGet("{position}/actions")]
        public async Task<ActionResult<IEnumerable<string>>> GetActionsAsync(int position)
        {
            PlantedPlant? plantAtPosition = await context.PlantHelper.GetPlantByPosition(position, CurrentUser);
            if (plantAtPosition == null)
            {
                return NotFound();
            }
            return await context.PlantHelper.GetActions(plantAtPosition);
        }

        [HttpGet("unlocked")]
        public async Task<ActionResult<IEnumerable<string>>> GetUnlockedCropsAsync()
        {
            int userXP = CurrentUser.UserXP;
            return await context.Products.Where(product => product.UnlockXP <= userXP && product.IsCrop)
                                    .Select(product => product.Name)
                                    .ToListAsync(); 
        }

        [HttpPost("{position}/{typeName}")]
        public async Task<ActionResult<PlantedPlant>> PostPlantedPlant(int position, string typeName)
        {
            if (await context.PlantHelper.GetPlantByPosition(position, CurrentUser) != null)
            {
                return Conflict("The field is not empty");
            }
            Product? productType = await context.ProductHelper.GetUnlockedCropByName(typeName, CurrentUser);
            if (productType == null)
            {
                return NotFound();
            }

            DateTimeOffset currentTime = DateTimeOffset.Now;
            int growTime = productType.ProductionTimeAsSeconds;
            PlantedPlant newPlant = context.PlantHelper.CreatePlantedPlant(typeName, position, growTime, CurrentUser);

            context.PlantedPlants.Add(newPlant);
            await context.SaveChangesAsync();

            return Ok();
        }

        [HttpPut("{position:int}/watering")]
        public async Task<IActionResult> PutWatering(int position)
        {
            PlantedPlant? plantAtPosition = await context.PlantHelper.GetPlantByPosition(position, CurrentUser);
            if (plantAtPosition == null)
            {
                return NotFound();
            }
            if (plantAtPosition.WateringTime == null)
            {
                return BadRequest("This plant got already watered.");
            }
            DateTimeOffset currentTime = DateTimeOffset.Now;
            if (currentTime >= plantAtPosition.WateringTime && (await context.PlantHelper.GetActions(plantAtPosition)).Contains("watering"))
            {
                plantAtPosition.WateringTime = null;
                plantAtPosition = context.PlantHelper.UpdateDateTimes(plantAtPosition);
            }
            try
            {
                await context.PlantHelper.UpdatePlantedPlantDatabase(plantAtPosition);
            }
            catch (NotFoundException ex)
            {
                return NotFound();
            }
            return NoContent();
        }
        [HttpPut("{position:int}/weeding")]
        public async Task<IActionResult> PutWeeding(int position)
        {
            PlantedPlant? plantAtPosition = await context.PlantHelper.GetPlantByPosition(position, CurrentUser);
            if (plantAtPosition == null)
            {
                return NotFound();
            }
            if (plantAtPosition.WeedingTime == null)
            {
                return BadRequest("This plant got already watered.");
            }
            DateTimeOffset currentTime = DateTimeOffset.Now;
            if (currentTime >= plantAtPosition.WeedingTime && (await context.PlantHelper.GetActions(plantAtPosition)).Contains("weeding"))
            {
                plantAtPosition.WeedingTime = null;
                plantAtPosition = context.PlantHelper.UpdateDateTimes(plantAtPosition);
            }
            try
            {
                await context.PlantHelper.UpdatePlantedPlantDatabase(plantAtPosition);
            }
            catch (NotFoundException ex)
            {
                return NotFound();
            }
            return NoContent();
        }

        [HttpPut("{position:int}/fertilising")]
        public async Task<IActionResult> PutFertilising(int position)
        {
            PlantedPlant? plantAtPosition = await context.PlantHelper.GetPlantByPosition(position, CurrentUser);
            if (plantAtPosition == null)
            {
                return NotFound();
            }
            if (plantAtPosition.FertilisingTime == null)
            {
                return BadRequest("This plant got already fertilised.");
            }
            DateTimeOffset currentTime = DateTimeOffset.Now;
            if (currentTime >= plantAtPosition.FertilisingTime && (await context.PlantHelper.GetActions(plantAtPosition)).Contains("fertilising"))
            {
                plantAtPosition.FertilisingTime = null;
                plantAtPosition = context.PlantHelper.UpdateDateTimes(plantAtPosition);
                await context.ProductHelper.AddUserProduct("other_manure", -1);
            }
            try
            {
                await context.PlantHelper.UpdatePlantedPlantDatabase(plantAtPosition);
            }
            catch (NotFoundException ex) {
                return NotFound();
            }
            return NoContent();
        }

        [HttpDelete("{position:int}/harvest")]
        public async Task<IActionResult> HarvestPlantedPlant(int position)
        {
            PlantedPlant? plantAtPosition = await context.PlantHelper.GetPlantByPosition(position, CurrentUser);
            if (plantAtPosition == null)
            {
                return NotFound();
            }
            Product? plantProduct = await context.ProductHelper.GetProductByName(plantAtPosition.CropsTypeName);
            if (plantProduct == null)
            {
                return NotFound();
            }
            DateTimeOffset currentTime = DateTimeOffset.Now;
            if (!(currentTime >= plantAtPosition.HarvestTime && (await context.PlantHelper.GetActions(plantAtPosition)).Contains("harvesting")))
            {
                return BadRequest("This plant can't be harvested yet.");
            }
            if((await context.PlantHelper.GetActions(plantAtPosition)).Count > 1)
            {
                return BadRequest("This plant needs other actions.");
            }
            context.PlantedPlants.Remove(plantAtPosition);
            await context.SaveChangesAsync();
            try
            {
                await context.ProductHelper.AddUserProduct(plantAtPosition.CropsTypeName, 3);
                await context.QuestHelper.ProgressQuest("harvest", plantAtPosition.CropsTypeName, 3, CurrentUser);
                CurrentUser.UserXP += plantProduct.RewardXP;
                await context.UserHelper.PutUser(CurrentUser.Id, CurrentUser);
            }
            catch (NotFoundException ex) {
                return NotFound(ex.Message);
            }
            catch (BadRequestException ex)
            {
                return BadRequest(ex.Message);
            }
            return NoContent();
        }
        
        [HttpGet ("plantedPlants")]
        public async Task<ActionResult<IEnumerable<PlantWithActions>?>> GetPlantedPlants()
        {
            var plantedPlants = await Helper.Helper.GetPlants(CurrentUser.Email, context);
            var plantWithActions = new List<PlantWithActions>();
            if (plantedPlants == null) return Ok(plantWithActions);
            
            foreach (var plant in plantedPlants)
            {
                var actions = await context.PlantHelper.GetActions(plant);
                plantWithActions.Add(new PlantWithActions {Plant = plant, Actions = actions});
            }
            
            return Ok(plantWithActions);
        }
    }
}