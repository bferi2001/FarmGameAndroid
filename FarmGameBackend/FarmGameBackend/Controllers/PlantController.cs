using FarmGameBackend.CustomExceptions;
using FarmGameBackend.DbContexts;
using FarmGameBackend.Entity;
using Microsoft.AspNetCore.Mvc;
using Microsoft.EntityFrameworkCore;

namespace FarmGameBackend.Controllers
{//ToDo Inventory and UserXP management
 //ToDo Fertilising costs animal poop resource
    [Route("api/farm/plant")]
    public class PlantController : Controller
    {
        private readonly FarmApplicationContext _context;
        private readonly User _currentUser;
        public PlantController(FarmApplicationContext context)
        {
            _context = context;
            //string? userEmail = HttpContext.Items["Email"]?.ToString();
            //_currentUser = _context.GetCurrentUser(userEmail!);
            _currentUser = _context.GetCurrentUser("testemail@gmail.com");
        }
        [HttpGet("{position}/actions")]
        public async Task<ActionResult<IEnumerable<string>>> GetActionsAsync(int position)
        {
            PlantedPlant? plantAtPosition = await _context.PlantHelper.GetPlantByPosition(position);
            if (plantAtPosition == null)
            {
                return NotFound();
            }
            return _context.PlantHelper.GetActions(plantAtPosition);
        }

        [HttpGet("unlocked")]
        public async Task<ActionResult<IEnumerable<string>>> GetUnlockedCropsAsync()
        {
            int userXP = _currentUser.UserXP;
            return await _context.Products.Where(product => product.UnlockXP <= userXP && product.IsCrop)
                                    .Select(product => product.Name)
                                    .ToListAsync(); 
        }

        [HttpPost("{position}/{typeName}")]
        public async Task<ActionResult<PlantedPlant>> PostPlantedPlant(int position, string typeName)
        {
            if (await _context.PlantHelper.GetPlantByPosition(position) != null)
            {
                return Conflict("The field is not empty");
            }
            Product? productType = await _context.ProductHelper.GetUnlockedCropByName(typeName);
            if (productType == null)
            {
                return NotFound();
            }

            DateTimeOffset currentTime = DateTimeOffset.Now;
            int growTime = productType.ProductionTimeAsSeconds;
            PlantedPlant newPlant = _context.PlantHelper.CreatePlantedPlant(typeName, position, growTime);

            _context.PlantedPlants.Add(newPlant);
            await _context.SaveChangesAsync();

            return Ok();
        }

        [HttpPut("{position:int}/watering")]
        public async Task<IActionResult> PutWatering(int position)
        {
            PlantedPlant? plantAtPosition = await _context.PlantHelper.GetPlantByPosition(position);
            if (plantAtPosition == null)
            {
                return NotFound();
            }
            if (plantAtPosition.WateringTime == null)
            {
                return BadRequest("This plant got already watered.");
            }
            DateTimeOffset currentTime = DateTimeOffset.Now;
            if (currentTime >= plantAtPosition.WateringTime && _context.PlantHelper.GetActions(plantAtPosition).Contains("watering"))
            {
                plantAtPosition.WateringTime = null;
                //ToDo
            }
            try
            {
                await _context.PlantHelper.UpdatePlantedPlantDatabase(plantAtPosition);
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
            PlantedPlant? plantAtPosition = await _context.PlantHelper.GetPlantByPosition(position);
            if (plantAtPosition == null)
            {
                return NotFound();
            }
            if (plantAtPosition.WeedingTime == null)
            {
                return BadRequest("This plant got already watered.");
            }
            DateTimeOffset currentTime = DateTimeOffset.Now;
            if (currentTime >= plantAtPosition.WeedingTime && _context.PlantHelper.GetActions(plantAtPosition).Contains("weeding"))
            {
                plantAtPosition.WeedingTime = null;
                //ToDo
            }
            try
            {
                await _context.PlantHelper.UpdatePlantedPlantDatabase(plantAtPosition);
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
            PlantedPlant? plantAtPosition = await _context.PlantHelper.GetPlantByPosition(position);
            if (plantAtPosition == null)
            {
                return NotFound();
            }
            if (plantAtPosition.FertilisingTime == null)
            {
                return BadRequest("This plant got already fertilised.");
            }
            DateTimeOffset currentTime = DateTimeOffset.Now;
            if (currentTime >= plantAtPosition.FertilisingTime && _context.PlantHelper.GetActions(plantAtPosition).Contains("fertilising"))
            {
                plantAtPosition.FertilisingTime = null;
                //ToDo Other actions?
            }
            try
            {
                await _context.PlantHelper.UpdatePlantedPlantDatabase(plantAtPosition);
            }
            catch (NotFoundException ex) {
                return NotFound();
            }
            return NoContent();
        }

        [HttpDelete("{position:int}/harvest")]
        public async Task<IActionResult> HarvestPlantedPlant(int position)
        {
            PlantedPlant? plantAtPosition = await _context.PlantHelper.GetPlantByPosition(position);
            if (plantAtPosition == null)
            {
                return NotFound();
            }
            DateTimeOffset currentTime = DateTimeOffset.Now;
            if (!(currentTime >= plantAtPosition.HarvestTime && _context.PlantHelper.GetActions(plantAtPosition).Contains("harvesting")))
            {
                return BadRequest("This plant can't be harvested yet.");
            }
            _context.PlantedPlants.Remove(plantAtPosition);
            await _context.SaveChangesAsync();
            try
            {
                await _context.ProductHelper.AddUserProduct(plantAtPosition.CropsTypeName, 3);
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
        public async Task<ActionResult<IEnumerable<PlantedPlant>?>> GetPlantedPlants()
        {
            return await Helper.Helper.GetPlants(_currentUser.Email, _context);
        }
    }
}