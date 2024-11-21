using FarmGameBackend.CustomExceptions;
using FarmGameBackend.DbContexts;
using FarmGameBackend.Entity;
using Microsoft.AspNetCore.Http;
using Microsoft.AspNetCore.Mvc;
using Microsoft.EntityFrameworkCore;
using static FarmGameBackend.Helper.ProductHelper;

namespace FarmGameBackend.Controllers
{
    [Route("api/farm")]
    public class MarketController : Controller
    {
        private readonly FarmApplicationContext _context;
        private readonly User _currentUser;
        public MarketController(FarmApplicationContext context)
        {
            _context = context;
            //string? userEmail = HttpContext.Items["Email"]?.ToString();
            //_currentUser = _context.GetCurrentUser(userEmail!);
            _currentUser = _context.GetCurrentUser("testemail@gmail.com");
        }


        [HttpGet("market")]
        public async Task<ActionResult<IEnumerable<Classified>>> GetClassifiedForMarketAsync()
        {
            return await _context.Classifieds.Where(classified => classified.Deadline > DateTimeOffset.Now).ToListAsync();
        }

        [HttpPost("market")]
        public async Task<ActionResult> PostClassified(/*Todo*/)
        {
            
        }

        [HttpDelete("market/{id}")]
        public async Task<IActionResult> BuyClassified(int classifiedId)
        {
            Classified classified = await GetClassified(classifiedId);
            if(classified == null)
            {
                return NotFound();
            }
            await ClassifiedDeleted(classified);
            return NoContent();
        }

        [HttpPut("market/quicksell/{productname}/{quantity}")]
        public async Task<IActionResult> QuicksellProduct(string productname, int quantity)
        {
            //ToDo
        }
        //ToDo szűréses lekérdezés
        //ToDo törlés, ha lejár
        public async Task<Classified> GetClassified(int id)
        {
            var classified = await _context.Classifieds.FindAsync(id);

            if (classified == null)
            {
                return null;
            }

            return classified;
        }

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
        public async Task<IActionResult> ClassifiedDeleted(Classified classified)
        {
            await DeleteClassified(classified.Id);
            User seller = _context.GetCurrentUser(classified.UserName);
            User buyer = _currentUser;
            await _context.ProductHelper.AddUserProduct(classified.ProductName, classified.Quantity);
            if (seller.Email != buyer.Email)
            {
                if (buyer.UserMoney < classified.Price)
                {
                    return BadRequest("Not enough money.");
                }
                seller.UserMoney += classified.Price;
                buyer.UserMoney -= classified.Price;



                _context.Entry(seller).State = EntityState.Modified;
                _context.Entry(buyer).State = EntityState.Modified;
                try
                {
                    await _context.SaveChangesAsync();
                }
                catch (DbUpdateConcurrencyException)
                {
                    if (!_context.Users.Any(e => e.Id == seller.Id))
                    {
                        return NotFound();
                    }
                    else if (!_context.Users.Any(e => e.Id == buyer.Id))
                    {
                        return NotFound();
                    }
                    else
                    {
                        throw;
                    }
                }
            }
            return NoContent();
        }
    }
}
