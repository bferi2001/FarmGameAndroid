using FarmGameBackend.CustomExceptions;
using FarmGameBackend.DbContexts;
using FarmGameBackend.Entity;
using Microsoft.AspNetCore.Http;
using Microsoft.AspNetCore.Mvc;
using Microsoft.EntityFrameworkCore;
using Microsoft.EntityFrameworkCore.Diagnostics.Internal;
using System.Reflection.Metadata.Ecma335;
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
            List<Classified> classifiedList = await _context.Classifieds.Where(classified => classified.Deadline <= DateTimeOffset.Now).ToListAsync();
            foreach (var item in classifiedList)
            {
                await ClassifiedDeleted(item);
            }
            return await _context.Classifieds.Where(classified => classified.Deadline > DateTimeOffset.Now).ToListAsync();
        }

        [HttpGet("market/{productName}")]
        public async Task<ActionResult<IEnumerable<Classified>>> GetClassifiedForMarketAsync(string productName)
        {
            List<Classified> classifiedList = await _context.Classifieds.Where(classified => classified.Deadline <= DateTimeOffset.Now && classified.ProductName == productName).ToListAsync();
            foreach (var item in classifiedList)
            {
                await ClassifiedDeleted(item);
            }
            return await _context.Classifieds.Where(classified => classified.Deadline > DateTimeOffset.Now).ToListAsync();
        }

        [HttpPost("market/{productName}/{quantity}/{price}")]
        public async Task<ActionResult> PostClassified(string productName, int quantity, int price)
        {
            if (!_context.ProductHelper.DoesProductExists(productName))
            {
                return NotFound();
            }
            try
            {
                Product? product = await _context.ProductHelper.GetProductByName(productName);
                UserProduct? userProduct = await _context.ProductHelper.GetUserProduct(productName);
                int ownedQuantity = userProduct.Quantity;

                await _context.ProductHelper.AddUserProduct(productName, -quantity);
                DateTime deadline = DateTime.UtcNow.Add(TimeSpan.FromHours(2*24));
                
                Classified classified = new Classified
                {
                    UserName = _currentUser.Email,
                    Price = price,
                    ProductName = productName,
                    Quantity = quantity,
                    Deadline = deadline,
                };

                await PostClassified(classified);
            }
            catch (NotFoundException ex)
            {
                return NotFound(ex.Message);
            }
            catch (BadRequestException ex)
            {
                return BadRequest(ex.Message);
            }

            return NoContent();
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

        [HttpPut("market/quicksell/{productName}/{quantity}")]
        public async Task<IActionResult> QuickSellProduct(string productName, int quantity)
        {
            if (!_context.ProductHelper.DoesProductExists(productName))
            {
                return NotFound();
            }
            try
            {
                Product? product = await _context.ProductHelper.GetProductByName(productName);
                UserProduct? userProduct = await _context.ProductHelper.GetUserProduct(productName);
                int ownedQuantity = userProduct.Quantity;

                await _context.ProductHelper.AddUserProduct(productName, -quantity);
                _currentUser.UserMoney += product.QuickSellPrice * quantity;
            }
            catch (NotFoundException ex)
            {
                return NotFound(ex.Message);
            }
            catch (BadRequestException ex)
            {
                return BadRequest(ex.Message);
            }

            return NoContent();
        }
        private async Task<Classified> PostClassified(Classified classified)
        {
            _context.Classifieds.Add(classified);
            await _context.SaveChangesAsync();

            return classified;
        }


        private async Task<Classified> GetClassified(int id)
        {
            var classified = await _context.Classifieds.FindAsync(id);

            if (classified == null)
            {
                return null;
            }

            return classified;
        }

        private async Task<IActionResult> DeleteClassified(int id)
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
        private async Task<IActionResult> ClassifiedDeleted(Classified classified)
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
