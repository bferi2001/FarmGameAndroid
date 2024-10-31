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
    public class ClassifiedsController : ControllerBase
    {
        private readonly FarmApplicationContext _context;

        public ClassifiedsController(FarmApplicationContext context)
        {
            _context = context;
        }

        // GET: api/Classifieds
        [HttpGet]
        public async Task<ActionResult<IEnumerable<Classified>>> GetClassifieds()
        {
            return await _context.Classifieds.ToListAsync();
        }

        // GET: api/Classifieds/5
        [HttpGet("{id}")]
        public async Task<ActionResult<Classified>> GetClassified(int id)
        {
            var classified = await _context.Classifieds.FindAsync(id);

            if (classified == null)
            {
                return NotFound();
            }

            return classified;
        }

        // PUT: api/Classifieds/5
        // To protect from overposting attacks, see https://go.microsoft.com/fwlink/?linkid=2123754
        [HttpPut("{id}")]
        public async Task<IActionResult> PutClassified(int id, Classified classified)
        {
            if (id != classified.Id)
            {
                return BadRequest();
            }

            _context.Entry(classified).State = EntityState.Modified;

            try
            {
                await _context.SaveChangesAsync();
            }
            catch (DbUpdateConcurrencyException)
            {
                if (!ClassifiedExists(id))
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

        // POST: api/Classifieds
        // To protect from overposting attacks, see https://go.microsoft.com/fwlink/?linkid=2123754
        [HttpPost]
        public async Task<ActionResult<Classified>> PostClassified(Classified classified)
        {
            _context.Classifieds.Add(classified);
            await _context.SaveChangesAsync();

            return CreatedAtAction("GetClassified", new { id = classified.Id }, classified);
        }

        // DELETE: api/Classifieds/5
        [HttpDelete("{id}")]
        public async Task<IActionResult> DeleteClassified(int id)
        {
            var classified = await _context.Classifieds.FindAsync(id);
            if (classified == null)
            {
                return NotFound();
            }

            _context.Classifieds.Remove(classified);
            await _context.SaveChangesAsync();

            return NoContent();
        }

        private bool ClassifiedExists(int id)
        {
            return _context.Classifieds.Any(e => e.Id == id);
        }
    }
}
