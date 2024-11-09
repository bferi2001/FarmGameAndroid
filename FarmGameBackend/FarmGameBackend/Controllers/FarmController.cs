using FarmGameBackend.DbContexts;
using FarmGameBackend.Entity;
using Microsoft.AspNetCore.Http.HttpResults;
using Microsoft.AspNetCore.Mvc;
using Microsoft.EntityFrameworkCore;

namespace FarmGameBackend.Controllers
{
    [Route("api/[controller]")]
    public class FarmController : Controller
    {
        private readonly FarmApplicationContext _context;

        public FarmController(FarmApplicationContext context)
        {
            _context = context;
        }
        [HttpGet("farm/actions/{position}")]
        public async Task<ActionResult<IEnumerable<string>>> GetActions(int position)
        {

        }

        [HttpGet("farm/plant/unlocked")]
        public async Task<ActionResult<IEnumerable<string>>> GetUnlockedCropsAsync()
        {
            
        }

        [HttpPost("farm/plant/{position}/{typeName}")]
        public async Task<ActionResult<PlantedPlant>> PostPlantedPlant(int position, string typeName)
        {
            if (GetPlantByPosition(position) != null)
            {
                return Conflict("The field is not empty");
            }
            Product? productType = GetUnlockedProductByName(typeName);
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
            PlantedPlant plantAtPosition = GetPlantByPosition(position);
            if (plantAtPosition == null)
            {
                return NotFound();
            }
            if (plantAtPosition.WateringTime == null)
            {
                return BadRequest("This plant got already watered.");
            }
            DateTimeOffset currentTime = DateTimeOffset.Now;
            if (currentTime >= plantAtPosition.WateringTime)
            {
                plantAtPosition.WateringTime = null;
                //ToDo
            }
            return await UpdatePlantedPlantDatabase(plantAtPosition);
        }
        [HttpPut("farm/weeding/{position}")]
        public async Task<IActionResult> PutWeeding(int position)
        {
            PlantedPlant plantAtPosition = GetPlantByPosition(position);
            if (plantAtPosition == null)
            {
                return NotFound();
            }
            if (plantAtPosition.WeedingTime == null)
            {
                return BadRequest("This plant got already watered.");
            }
            DateTimeOffset currentTime = DateTimeOffset.Now;
            if (currentTime >= plantAtPosition.WeedingTime)
            {
                plantAtPosition.WeedingTime = null;
                //ToDo
            }
            return await UpdatePlantedPlantDatabase(plantAtPosition);
        }

        [HttpPut("farm/fertilising/{position}")]
        public async Task<IActionResult> PutFertilising(int position)
        {
            PlantedPlant plantAtPosition = GetPlantByPosition(position);
            if (plantAtPosition == null)
            {
                return NotFound();
            }
            if (plantAtPosition.FertilisingTime == null)
            {
                return BadRequest("This plant got already fertilised.");
            }
            DateTimeOffset currentTime = DateTimeOffset.Now;
            if (currentTime >= plantAtPosition.FertilisingTime)
            {
                plantAtPosition.FertilisingTime = null;
                //ToDo Other actions?
            }
            return await UpdatePlantedPlantDatabase(plantAtPosition);
        }

        [HttpDelete("farm/collecting/{id}")]
        public async Task<IActionResult> DeletePlantedPlant(int id)
        {

        }
        public async Task<IActionResult> UpdatePlantedPlantDatabase(PlantedPlant updatedPlant)
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
        public Product? GetUnlockedProductByName(string name)
        {
            if (!DoesCroptypeExistsAndUnlocked(name))
            {
                return null;
            }
            Product? productType = _context.Products.Where(product => product.Name == name).First();
            return productType;
        }

        public Boolean DoesProductExists(string _productName)
        {
            return _context.Products.Any(product => product.Name == _productName);
        }

        public List<string> GetUnlockedCrops()
        {
            int userXP = _context.GetCurrentUser().UserXP;
            return _context.Products.Where(product => product.UnlockXP >= userXP && product.IsCrop)
                                    .Select(product => product.Name)
                                    .ToList();
        }

        public Boolean DoesCroptypeExistsAndUnlocked(string _cropName)
        {
            return GetUnlockedCrops().Any(cropName => cropName == _cropName);
        }
        public PlantedPlant GetPlantByPosition(int position)
        {
            return _context.PlantedPlants.Where(plant => plant.Position == position && plant.UserName == _context.GetCurrentUser().Email).First();
        }

        public PlantedPlant CreatePlantedPlant(string _cropsTypeName, int _position, int _growTime)
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