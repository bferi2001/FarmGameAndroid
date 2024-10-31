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
    public class UserProductsController : ControllerBase
    {
        private readonly FarmApplicationContext _context;

        public UserProductsController(FarmApplicationContext context)
        {
            _context = context;
        }

        // GET: api/UserProducts
        [HttpGet]
        public async Task<ActionResult<IEnumerable<UserProduct>>> GetUserProduct()
        {
            return await _context.UserProduct.ToListAsync();
        }

        // GET: api/UserProducts/5
        [HttpGet("{id}")]
        public async Task<ActionResult<UserProduct>> GetUserProduct(int id)
        {
            var userProduct = await _context.UserProduct.FindAsync(id);

            if (userProduct == null)
            {
                return NotFound();
            }

            return userProduct;
        }

        // PUT: api/UserProducts/5
        // To protect from overposting attacks, see https://go.microsoft.com/fwlink/?linkid=2123754
        [HttpPut("{id}")]
        public async Task<IActionResult> PutUserProduct(int id, UserProduct userProduct)
        {
            if (id != userProduct.Id)
            {
                return BadRequest();
            }

            _context.Entry(userProduct).State = EntityState.Modified;

            try
            {
                await _context.SaveChangesAsync();
            }
            catch (DbUpdateConcurrencyException)
            {
                if (!UserProductExists(id))
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

        // POST: api/UserProducts
        // To protect from overposting attacks, see https://go.microsoft.com/fwlink/?linkid=2123754
        [HttpPost]
        public async Task<ActionResult<UserProduct>> PostUserProduct(UserProduct userProduct)
        {
            _context.UserProduct.Add(userProduct);
            await _context.SaveChangesAsync();

            return CreatedAtAction("GetUserProduct", new { id = userProduct.Id }, userProduct);
        }

        // DELETE: api/UserProducts/5
        [HttpDelete("{id}")]
        public async Task<IActionResult> DeleteUserProduct(int id)
        {
            var userProduct = await _context.UserProduct.FindAsync(id);
            if (userProduct == null)
            {
                return NotFound();
            }

            _context.UserProduct.Remove(userProduct);
            await _context.SaveChangesAsync();

            return NoContent();
        }

        private bool UserProductExists(int id)
        {
            return _context.UserProduct.Any(e => e.Id == id);
        }
    }
}
