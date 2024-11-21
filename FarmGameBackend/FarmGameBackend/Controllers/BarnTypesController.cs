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
        
        [HttpGet("test")]
        public ActionResult Test()
        {
            return Ok("BarnTypesController is working.");
        }

        // GET: api/BarnTypes
        [HttpGet]
        public async Task<ActionResult<IEnumerable<BarnType>>> GetBarnTypes()
        {
            return await _context.BarnTypes.ToListAsync();
        }

        // GET: api/BarnTypes/names
        [HttpGet("names")]
        public async Task<ActionResult<List<string>>> GetBarnTypeNames()
        {
            var barnTypeNames = await _context.BarnTypes.Select(barnType => barnType.Name).ToListAsync();

            if (barnTypeNames == null)
            {
                return NotFound();
            }

            return barnTypeNames;
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

        // GET: api/BarnTypes/5/name
        [HttpGet("{id}/name")]
        public async Task<ActionResult<string>> GetBarnTypeName(int id)
        {
            var barnType = await _context.BarnTypes.FindAsync(id);

            if (barnType == null)
            {
                return NotFound();
            }

            return barnType.Name;
        }

        // GET: api/BarnTypes/5/2
        [HttpGet("{id}/{level}")]
        public async Task<ActionResult<int>> GetBarnTypeByLevel(int id, int level)
        {
            var barnType = await _context.BarnTypes.FindAsync(id);

            if (barnType == null)
            {
                return NotFound();
            }
            switch (level)
            {
                case 1:
                    return barnType.FirstUpgradeCost;
                case 2:
                    return barnType.SecondUpgradeCost;
                case 3:
                    return barnType.ThirdUpgradeCost;
                default:
                    return NotFound();
            }
        }

        private bool BarnTypeExists(int id)
        {
            return _context.BarnTypes.Any(e => e.Id == id);
        }
    }
}
