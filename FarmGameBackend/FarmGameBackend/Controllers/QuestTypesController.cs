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
    public class QuestTypesController : ControllerBase
    {
        private readonly FarmApplicationContext _context;

        public QuestTypesController(FarmApplicationContext context)
        {
            _context = context;
        }

        // GET: api/QuestTypes
        [HttpGet]
        public async Task<ActionResult<IEnumerable<QuestType>>> GetQuestTypes()
        {
            return await _context.QuestTypes.ToListAsync();
        }

        // GET: api/QuestTypes/5
        [HttpGet("{id}")]
        public async Task<ActionResult<QuestType>> GetQuestType(int id)
        {
            var questType = await _context.QuestTypes.FindAsync(id);

            if (questType == null)
            {
                return NotFound();
            }

            return questType;
        }

        // PUT: api/QuestTypes/5
        // To protect from overposting attacks, see https://go.microsoft.com/fwlink/?linkid=2123754
        [HttpPut("{id}")]
        public async Task<IActionResult> PutQuestType(int id, QuestType questType)
        {
            if (id != questType.Id)
            {
                return BadRequest();
            }

            _context.Entry(questType).State = EntityState.Modified;

            try
            {
                await _context.SaveChangesAsync();
            }
            catch (DbUpdateConcurrencyException)
            {
                if (!QuestTypeExists(id))
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

        // POST: api/QuestTypes
        // To protect from overposting attacks, see https://go.microsoft.com/fwlink/?linkid=2123754
        [HttpPost]
        public async Task<ActionResult<QuestType>> PostQuestType(QuestType questType)
        {
            _context.QuestTypes.Add(questType);
            await _context.SaveChangesAsync();

            return CreatedAtAction("GetQuestType", new { id = questType.Id }, questType);
        }

        // DELETE: api/QuestTypes/5
        [HttpDelete("{id}")]
        public async Task<IActionResult> DeleteQuestType(int id)
        {
            var questType = await _context.QuestTypes.FindAsync(id);
            if (questType == null)
            {
                return NotFound();
            }

            _context.QuestTypes.Remove(questType);
            await _context.SaveChangesAsync();

            return NoContent();
        }

        private bool QuestTypeExists(int id)
        {
            return _context.QuestTypes.Any(e => e.Id == id);
        }
    }
}
