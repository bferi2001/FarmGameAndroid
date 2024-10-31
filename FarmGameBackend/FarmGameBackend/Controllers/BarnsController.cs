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
    public class BarnsController : ControllerBase
    {
        private readonly FarmApplicationContext _context;

        public BarnsController(FarmApplicationContext context)
        {
            _context = context;
        }

        // GET: api/Barns
        [HttpGet]
        public async Task<ActionResult<IEnumerable<Barn>>> GetBarns()
        {
            return await _context.Barns.ToListAsync();
        }

        // GET: api/Barns/5
        [HttpGet("{id}")]
        public async Task<ActionResult<Barn>> GetBarn(int id)
        {
            var barn = await _context.Barns.FindAsync(id);

            if (barn == null)
            {
                return NotFound();
            }

            return barn;
        }

        // PUT: api/Barns/5
        // To protect from overposting attacks, see https://go.microsoft.com/fwlink/?linkid=2123754
        [HttpPut("{id}")]
        public async Task<IActionResult> PutBarn(int id, Barn barn)
        {
            if (id != barn.Id)
            {
                return BadRequest();
            }

            _context.Entry(barn).State = EntityState.Modified;

            try
            {
                await _context.SaveChangesAsync();
            }
            catch (DbUpdateConcurrencyException)
            {
                if (!BarnExists(id))
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

        // POST: api/Barns
        // To protect from overposting attacks, see https://go.microsoft.com/fwlink/?linkid=2123754
        [HttpPost]
        public async Task<ActionResult<Barn>> PostBarn(Barn barn)
        {
            _context.Barns.Add(barn);
            await _context.SaveChangesAsync();

            return CreatedAtAction("GetBarn", new { id = barn.Id }, barn);
        }

        // DELETE: api/Barns/5
        [HttpDelete("{id}")]
        public async Task<IActionResult> DeleteBarn(int id)
        {
            var barn = await _context.Barns.FindAsync(id);
            if (barn == null)
            {
                return NotFound();
            }

            _context.Barns.Remove(barn);
            await _context.SaveChangesAsync();

            return NoContent();
        }

        private bool BarnExists(int id)
        {
            return _context.Barns.Any(e => e.Id == id);
        }
    }
}
