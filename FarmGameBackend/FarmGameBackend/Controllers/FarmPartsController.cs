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
    public class FarmPartsController : ControllerBase
    {
        private readonly FarmApplicationContext _context;

        public FarmPartsController(FarmApplicationContext context)
        {
            _context = context;
        }

        // GET: api/FarmParts
        [HttpGet]
        public async Task<ActionResult<IEnumerable<FarmPart>>> GetFarmParts()
        {
            return await _context.FarmParts.ToListAsync();
        }

        // GET: api/FarmParts/5
        [HttpGet("{id}")]
        public async Task<ActionResult<FarmPart>> GetFarmPart(int id)
        {
            var farmPart = await _context.FarmParts.FindAsync(id);

            if (farmPart == null)
            {
                return NotFound();
            }

            return farmPart;
        }

        // PUT: api/FarmParts/5
        // To protect from overposting attacks, see https://go.microsoft.com/fwlink/?linkid=2123754
        [HttpPut("{id}")]
        public async Task<IActionResult> PutFarmPart(int id, FarmPart farmPart)
        {
            if (id != farmPart.Id)
            {
                return BadRequest();
            }

            _context.Entry(farmPart).State = EntityState.Modified;

            try
            {
                await _context.SaveChangesAsync();
            }
            catch (DbUpdateConcurrencyException)
            {
                if (!FarmPartExists(id))
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

        // POST: api/FarmParts
        // To protect from overposting attacks, see https://go.microsoft.com/fwlink/?linkid=2123754
        [HttpPost]
        public async Task<ActionResult<FarmPart>> PostFarmPart(FarmPart farmPart)
        {
            _context.FarmParts.Add(farmPart);
            await _context.SaveChangesAsync();

            return CreatedAtAction("GetFarmPart", new { id = farmPart.Id }, farmPart);
        }

        // DELETE: api/FarmParts/5
        [HttpDelete("{id}")]
        public async Task<IActionResult> DeleteFarmPart(int id)
        {
            var farmPart = await _context.FarmParts.FindAsync(id);
            if (farmPart == null)
            {
                return NotFound();
            }

            _context.FarmParts.Remove(farmPart);
            await _context.SaveChangesAsync();

            return NoContent();
        }

        private bool FarmPartExists(int id)
        {
            return _context.FarmParts.Any(e => e.Id == id);
        }
    }
}
