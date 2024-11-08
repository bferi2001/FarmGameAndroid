using System;
using System.Collections.Generic;
using System.Linq;
using System.Threading.Tasks;
using Microsoft.AspNetCore.Http;
using Microsoft.AspNetCore.Mvc;
using Microsoft.EntityFrameworkCore;
using FarmGameBackend.DbContexts;
using FarmGameBackend.Entity;

namespace FarmGameBackend.Controllers
{
    [Route("api/[controller]")]
    [ApiController]
    public class PlantedPlantsController : ControllerBase
    {
        private readonly FarmApplicationContext _context;

        public PlantedPlantsController(FarmApplicationContext context)
        {
            _context = context;
        }

        // GET: api/PlantedPlants
        [HttpGet]
        public async Task<ActionResult<IEnumerable<PlantedPlant>>> GetPlantedPlants()
        {
            return await _context.PlantedPlants.ToListAsync();
        }

        // GET: api/PlantedPlants/5
        [HttpGet("{id}")]
        public async Task<ActionResult<PlantedPlant>> GetPlantedPlant(int id)
        {
            var plantedPlant = await _context.PlantedPlants.FindAsync(id);

            if (plantedPlant == null)
            {
                return NotFound();
            }

            return plantedPlant;
        }

        // PUT: api/PlantedPlants/5
        // To protect from overposting attacks, see https://go.microsoft.com/fwlink/?linkid=2123754
        [HttpPut("{id}")]
        public async Task<IActionResult> PutPlantedPlant(int id, PlantedPlant plantedPlant)
        {
            if (id != plantedPlant.Id)
            {
                return BadRequest();
            }

            _context.Entry(plantedPlant).State = EntityState.Modified;

            try
            {
                await _context.SaveChangesAsync();
            }
            catch (DbUpdateConcurrencyException)
            {
                if (!PlantedPlantExists(id))
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

        // POST: api/PlantedPlants
        // To protect from overposting attacks, see https://go.microsoft.com/fwlink/?linkid=2123754
        [HttpPost("{typeId}/{position}")]
        public async Task<ActionResult<PlantedPlant>> PostPlantedPlant(int typeId, int position)
        {
            DateTimeOffset currentTime = DateTimeOffset.Now;
            var newPlant = new PlantedPlant();
            newPlant.TypeId = typeId;
            newPlant.Position = position;
            newPlant.PlantTime = currentTime;
            Product productType = await _context.Products.FindAsync(newPlant.TypeId);
            if (productType == null)
            {
                return NotFound();
            }
            Random r = new Random();
            int growTime = productType.ProductionTimeAsSeconds;
            newPlant.HarvestTime = currentTime.AddSeconds(growTime);
            newPlant.WateringTime = currentTime.AddSeconds(r.Next(growTime));
            newPlant.WeedingTime = currentTime.AddSeconds(r.Next(growTime));
            newPlant.FertilisingTime = currentTime.AddSeconds(r.Next(growTime));
            newPlant.UserId = 0; //ToDo

            _context.PlantedPlants.Add(newPlant);
            await _context.SaveChangesAsync();

            return CreatedAtAction("GetPlantedPlant", new { id = newPlant.Id }, newPlant);
        }

        // DELETE: api/PlantedPlants/5
        [HttpDelete("{id}")]
        public async Task<IActionResult> DeletePlantedPlant(int id)
        {
            var plantedPlant = await _context.PlantedPlants.FindAsync(id);
            if (plantedPlant == null)
            {
                return NotFound();
            }

            _context.PlantedPlants.Remove(plantedPlant);
            await _context.SaveChangesAsync();

            return NoContent();
        }

        private bool PlantedPlantExists(int id)
        {
            return _context.PlantedPlants.Any(e => e.Id == id);
        }
    }
}
