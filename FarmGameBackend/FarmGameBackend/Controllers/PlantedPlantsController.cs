﻿using System;
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
        [HttpPost]
        public async Task<ActionResult<PlantedPlant>> PostPlantedPlant(PlantedPlant plantedPlant)
        {
            DateTimeOffset currentTime = DateTimeOffset.Now;

            _context.PlantedPlants.Add(plantedPlant);
            await _context.SaveChangesAsync();

            return CreatedAtAction("GetPlantedPlant", new { id = plantedPlant.Id }, plantedPlant);
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