using Azure.Identity;
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
        public async Task<ActionResult<IEnumerable<MarketUserProduct>>> GetUserProductsForMarketAsync()
        {
            //ToDo
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

                if (ownedQuantity - quantity < 0)
                {
                    return BadRequest("Not enough product!");
                }
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

        [HttpDelete("market/buy/{id}")]
        public async Task<IActionResult> BuyClassified(int íd)
        {
            //ToDo
        }

        [HttpDelete("market/cancel/{id}")]
        public async Task<IActionResult> CancelClassified(int íd)
        {
            //ToDo
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

                if (ownedQuantity - quantity < 0)

                {
                    return BadRequest("Not enough product!");
                }

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
        //ToDo szűréses lekérdezés
        //ToDo törlés, ha lejár
        
        // MarketCOotorllerHelper
        private async Task<Classified> PostClassified(Classified classified)
        {
            _context.Classifieds.Add(classified);
            await _context.SaveChangesAsync();

            return classified;
        }


    }
}
