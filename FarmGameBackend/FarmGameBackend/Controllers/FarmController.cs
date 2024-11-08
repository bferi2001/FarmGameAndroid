using FarmGameBackend.DbContexts;
using FarmGameBackend.Entity;
using Microsoft.AspNetCore.Mvc;

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
        public async Task<ActionResult<IEnumerable<string>>> GetPlantedPlant(int position)
        {

        }

        [HttpPost("farm/plant/{position}/{typeName}")]
        public async Task<ActionResult<PlantedPlant>> PostFarm(int position, int typeName)
        {
            
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
