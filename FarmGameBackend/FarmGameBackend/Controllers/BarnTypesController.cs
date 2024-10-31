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
    public class BarnTypesController : ControllerBase
    {
        private readonly FarmApplicationContext _context;

        public BarnTypesController(FarmApplicationContext context)
        {
            _context = context;
        }

        // GET: api/BarnTypes
        [HttpGet]
        public async Task<ActionResult<IEnumerable<BarnType>>> GetBarnTypes()
        {
            return await _context.BarnTypes.ToListAsync();
        }

        // GET: api/BarnTypes/5
        [HttpGet("{id}")]
        public async Task<ActionResult<BarnType>> GetBarnType(int id)
        {
            var barnType = await _context.BarnTypes.FindAsync(id);

            if (barnType == null)
            {
                return NotFound();
            }

            return barnType;
        }

        // PUT: api/BarnTypes/5
        // To protect from overposting attacks, see https://go.microsoft.com/fwlink/?linkid=2123754
        [HttpPut("{id}")]
        public async Task<IActionResult> PutBarnType(int id, BarnType barnType)
        {
            if (id != barnType.Id)
            {
                return BadRequest();
            }

            _context.Entry(barnType).State = EntityState.Modified;

            try
            {
                await _context.SaveChangesAsync();
            }
            catch (DbUpdateConcurrencyException)
            {
                if (!BarnTypeExists(id))
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

        // POST: api/BarnTypes
        // To protect from overposting attacks, see https://go.microsoft.com/fwlink/?linkid=2123754
        [HttpPost]
        public async Task<ActionResult<BarnType>> PostBarnType(BarnType barnType)
        {
            _context.BarnTypes.Add(barnType);
            await _context.SaveChangesAsync();

            return CreatedAtAction("GetBarnType", new { id = barnType.Id }, barnType);
        }

        // DELETE: api/BarnTypes/5
        [HttpDelete("{id}")]
        public async Task<IActionResult> DeleteBarnType(int id)
        {
            var barnType = await _context.BarnTypes.FindAsync(id);
            if (barnType == null)
            {
                return NotFound();
            }

            _context.BarnTypes.Remove(barnType);
            await _context.SaveChangesAsync();

            return NoContent();
        }

        private bool BarnTypeExists(int id)
        {
            return _context.BarnTypes.Any(e => e.Id == id);
        }
    }
}
