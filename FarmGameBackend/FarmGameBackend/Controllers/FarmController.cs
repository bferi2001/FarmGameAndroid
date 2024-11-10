using FarmGameBackend.DbContexts;
using FarmGameBackend.Entity;
using Microsoft.AspNetCore.Http.HttpResults;
using Microsoft.AspNetCore.Mvc;
using Microsoft.EntityFrameworkCore;

namespace FarmGameBackend.Controllers
{//ToDo Inventory and UserXP management
 //ToDo Fertilising costs animal poop resource
    [Route("api/[controller]")]
    public class FarmController : Controller
    {
        private readonly FarmApplicationContext _context;

        public FarmController(FarmApplicationContext context)
        {
            _context = context;
        }
        [HttpGet("farm/plant/{position}/actions")]
        public async Task<ActionResult<IEnumerable<string>>> GetActionsAsync(int position)
        {
            PlantedPlant plantAtPosition = await GetPlantByPosition(position);
            if (plantAtPosition == null)
            {
                return NotFound();
            }
            return GetActions(plantAtPosition);
        }

        /*[HttpGet("farm/plant/unlocked")]
        public async Task<ActionResult<IEnumerable<string>>> GetUnlockedCropsAsync()
        {
            
        }*/

        [HttpPost("farm/plant/{position}/{typeName}")]
        public async Task<ActionResult<PlantedPlant>> PostPlantedPlant(int position, string typeName)
        {
            if (GetPlantByPosition(position) != null)
            {
                return Conflict("The field is not empty");
            }
            Product? productType = await GetUnlockedProductByName(typeName);
            if (productType == null)
            {
                return NotFound();
            }

            DateTimeOffset currentTime = DateTimeOffset.Now;
            int growTime = productType.ProductionTimeAsSeconds;
            PlantedPlant newPlant = CreatePlantedPlant(typeName, position, growTime);

            _context.PlantedPlants.Add(newPlant);
            await _context.SaveChangesAsync();

            return CreatedAtAction("GetPlantedPlant", new { id = newPlant.Id }, newPlant);
        }

        [HttpPut("farm/watering/{position}")]
        public async Task<IActionResult> PutWatering(int position)
        {
            PlantedPlant plantAtPosition = await GetPlantByPosition(position);
            if (plantAtPosition == null)
            {
                return NotFound();
            }
            if (plantAtPosition.WateringTime == null)
            {
                return BadRequest("This plant got already watered.");
            }
            DateTimeOffset currentTime = DateTimeOffset.Now;
            if (currentTime >= plantAtPosition.WateringTime && GetActions(plantAtPosition).Contains("watering"))
            {
                plantAtPosition.WateringTime = null;
                //ToDo
            }
            return await UpdatePlantedPlantDatabase(plantAtPosition);
        }
        [HttpPut("farm/weeding/{position}")]
        public async Task<IActionResult> PutWeeding(int position)
        {
            PlantedPlant plantAtPosition = await GetPlantByPosition(position);
            if (plantAtPosition == null)
            {
                return NotFound();
            }
            if (plantAtPosition.WeedingTime == null)
            {
                return BadRequest("This plant got already watered.");
            }
            DateTimeOffset currentTime = DateTimeOffset.Now;
            if (currentTime >= plantAtPosition.WeedingTime && GetActions(plantAtPosition).Contains("weeding"))
            {
                plantAtPosition.WeedingTime = null;
                //ToDo
            }
            return await UpdatePlantedPlantDatabase(plantAtPosition);
        }

        [HttpPut("farm/fertilising/{position}")]
        public async Task<IActionResult> PutFertilising(int position)
        {
            PlantedPlant plantAtPosition = await GetPlantByPosition(position);
            if (plantAtPosition == null)
            {
                return NotFound();
            }
            if (plantAtPosition.FertilisingTime == null)
            {
                return BadRequest("This plant got already fertilised.");
            }
            DateTimeOffset currentTime = DateTimeOffset.Now;
            if (currentTime >= plantAtPosition.FertilisingTime && GetActions(plantAtPosition).Contains("fertilising"))
            {
                plantAtPosition.FertilisingTime = null;
                //ToDo Other actions?
            }
            return await UpdatePlantedPlantDatabase(plantAtPosition);
        }

        /*[HttpDelete("farm/collecting/{id}")]
        public async Task<IActionResult> DeletePlantedPlant(int id)
        {

        }*/
        private  List<string> GetActions(PlantedPlant plantedPlant)
        {
            List<string> actions = new List<string>();
            if (plantedPlant.HarvestTime != null && plantedPlant.HarvestTime < DateTimeOffset.Now)
            {
                actions.Add("harvesting");
                return actions;
            }
            if (plantedPlant.WateringTime != null && plantedPlant.WateringTime < DateTimeOffset.Now)
            {
                actions.Add("watering");
            }
            if (plantedPlant.WeedingTime != null && plantedPlant.WeedingTime < DateTimeOffset.Now)
            {
                actions.Add("weeding");
            }
            if (plantedPlant.FertilisingTime != null && plantedPlant.FertilisingTime < DateTimeOffset.Now)
            {
                actions.Add("fertilising");
            }
            return actions;
        }

        private async Task<IActionResult> UpdatePlantedPlantDatabase(PlantedPlant updatedPlant)
        {
            _context.Entry(updatedPlant).State = EntityState.Modified;
            try
            {
                await _context.SaveChangesAsync();
            }
            catch (DbUpdateConcurrencyException)
            {
                if (!ProductExists(updatedPlant.Id))
                {
                    return NotFound();
                }
                else
                {
                    throw;
                }
            }
            return NoContent();
        }
        private async Task<Product?> GetUnlockedProductByName(string name)
        {
            if (!DoesCroptypeExistsAndUnlocked(name))
            {
                return null;
            }
            Product? productType = await _context.Products.Where(product => product.Name == name).FirstAsync();
            return productType;
        }

        private Boolean DoesProductExists(string _productName)
        {
            return _context.Products.Any(product => product.Name == _productName);
        }
        private List<string> GetUnlockedCrops()
        {
            int userXP = _context.GetCurrentUser().UserXP;
            return _context.Products.Where(product => product.UnlockXP <= userXP && product.IsCrop)
                                    .Select(product => product.Name)
                                    .ToList();
        }

        private Boolean DoesCroptypeExistsAndUnlocked(string _cropName)
        {
            return GetUnlockedCrops().Any(cropName => cropName == _cropName);
        }
        private async Task<PlantedPlant> GetPlantByPosition(int position)
        {
            try
            {
                return await _context.PlantedPlants.Where(plant => plant.Position == position && plant.UserName == _context.GetCurrentUser().Email).FirstAsync();
            }catch (Exception ex)
            {
                return null;
            }
            
        }

        private PlantedPlant CreatePlantedPlant(string _cropsTypeName, int _position, int _growTime)
        {
            Random r = new Random();
            DateTimeOffset currentTime = DateTimeOffset.Now;
            return new PlantedPlant
            {
                UserName = _context.GetCurrentUser().Email,
                CropsTypeName = _cropsTypeName,
                Position = _position,
                PlantTime = currentTime,
                HarvestTime = currentTime.AddSeconds(_growTime),
                WateringTime = currentTime.AddSeconds(r.Next(_growTime)),
                WeedingTime = currentTime.AddSeconds(r.Next(_growTime)),
                FertilisingTime = currentTime.AddSeconds(r.Next(_growTime))
            };
        }
        private bool ProductExists(int id)
        {
            return _context.Products.Any(e => e.Id == id);
        }
    }
}