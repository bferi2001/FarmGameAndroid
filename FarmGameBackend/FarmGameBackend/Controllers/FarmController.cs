using FarmGameBackend.DbContexts;
using FarmGameBackend.Entity;
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

        [HttpPost("farm/plant/{position}/{typeName}")]
        public async Task<ActionResult<PlantedPlant>> PostPlantedPlant(int position, string typeName)
        {
            DateTimeOffset currentTime = DateTimeOffset.Now;
            Product? productType = getProductByName(typeName);
            if(productType == null)
            {
                return NotFound();
            }
            //ToDo Check if crop is unlocked
            int growTime = productType.ProductionTimeAsSeconds;
            var newPlant = new PlantedPlant(typeName, position, growTime);

            _context.PlantedPlants.Add(newPlant);
            await _context.SaveChangesAsync();

            return CreatedAtAction("GetPlantedPlant", new { id = newPlant.Id }, newPlant);
        }
        public Product? getProductByName(string name)
        {
            if (!DoesProductExists(name))
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

        public List<string> GetUnlockedCrops(int _userXP)
        {
            return _context.Products.Where(product => product.UnlockXP>= _userXP && product.IsCrop)
                                    .Select(product => product.Name)
                                    .ToList();
        }

        public Boolean IsCropUnlocked(int _userXP, string _cropName)
        {
            return GetUnlockedCrops(_userXP).Any(cropName => cropName == _cropName);
        }

        [HttpPut("farm/watering/{position}")]
        public async Task<IActionResult> PutWatering(int position)
        {

        }
        [HttpPut("farm/weeding/{position}")]
        public async Task<IActionResult> PutWeeding(int position)
        {

        }

        [HttpPut("farm/fertilising/{position}")]
        public async Task<IActionResult> PutFertilising(int position)
        {

        }

        [HttpDelete("farm/collecting/{id}")]
        public async Task<IActionResult> DeletePlantedPlant(int id)
        {
            
        }

    }
}
