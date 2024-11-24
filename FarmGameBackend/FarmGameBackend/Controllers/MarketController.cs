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
    public class MarketController(FarmApplicationContext context) : Controller
    {
        private User CurrentUser
        {
            get
            {
                if(HttpContext.Items["CurrentUser"] == null)
                {
                    throw new BadRequestException("User is not logged in.");
                }
                return (User)HttpContext.Items["CurrentUser"]!;
            }
        }


        [HttpGet("market")]
        public async Task<ActionResult<IEnumerable<Classified>>> GetClassifiedForMarketAsync()
        {
            List<Classified> classifiedList = await context.Classifieds.Where(classified => classified.Deadline <= DateTimeOffset.Now).ToListAsync();
            foreach (var item in classifiedList)
            {
                await ClassifiedDeleted(item);
            }
            return await context.Classifieds.Where(classified => classified.Deadline > DateTimeOffset.Now).ToListAsync();
        }

        [HttpGet("market/{productName}")]
        public async Task<ActionResult<IEnumerable<Classified>>> GetClassifiedForMarketAsync(string productName)
        {
            List<Classified> classifiedList = await context.Classifieds.Where(classified => classified.Deadline <= DateTimeOffset.Now && classified.ProductName == productName).ToListAsync();
            foreach (var item in classifiedList)
            {
                await ClassifiedDeleted(item);
            }
            return await context.Classifieds.Where(classified => classified.Deadline > DateTimeOffset.Now).ToListAsync();
        }

        [HttpPost("market/{productName}/{quantity}/{price}")]
        public async Task<ActionResult> PostClassified(string productName, int quantity, int price)
        {
            if (!context.ProductHelper.DoesProductExists(productName))
            {
                return NotFound();
            }
            try
            {
                Product? product = await context.ProductHelper.GetProductByName(productName);
                UserProduct? userProduct = await context.ProductHelper.GetUserProduct(productName);
                int ownedQuantity = userProduct.Quantity;

                await context.ProductHelper.AddUserProduct(productName, -quantity);
                DateTime deadline = DateTime.UtcNow.Add(TimeSpan.FromHours(2*24));
                
                Classified classified = new Classified
                {
                    UserName = CurrentUser.Email,
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


        [HttpDelete("market/{classifiedId}")]
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
            if (!context.ProductHelper.DoesProductExists(productName))
            {
                return NotFound();
            }
            try
            {
                Product? product = await context.ProductHelper.GetProductByName(productName);
                UserProduct? userProduct = await context.ProductHelper.GetUserProduct(productName);
                int ownedQuantity = userProduct.Quantity;

                await context.ProductHelper.AddUserProduct(productName, -quantity);
                CurrentUser.UserMoney += product.QuickSellPrice * quantity;
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
            context.Classifieds.Add(classified);
            await context.SaveChangesAsync();

            return classified;
        }


        private async Task<Classified> GetClassified(int id)
        {
            var classified = await context.Classifieds.FindAsync(id);

            if (classified == null)
            {
                return null;
            }

            return classified;
        }

        private async Task<IActionResult> DeleteClassified(int id)
        {
            var classified = await context.Classifieds.FindAsync(id);
            if (classified == null)
            {
                return NotFound();
            }

            context.Classifieds.Remove(classified);
            await context.SaveChangesAsync();

            return NoContent();
        }
        private async Task<IActionResult> ClassifiedDeleted(Classified classified)
        {
            await DeleteClassified(classified.Id);
            User seller = context.GetCurrentUser(classified.UserName);
            User buyer = CurrentUser;
            await context.ProductHelper.AddUserProduct(classified.ProductName, classified.Quantity);
            if (seller.Email != buyer.Email)
            {
                if (buyer.UserMoney < classified.Price)
                {
                    return BadRequest("Not enough money.");
                }
                seller.UserMoney += classified.Price;
                buyer.UserMoney -= classified.Price;



                context.Entry(seller).State = EntityState.Modified;
                context.Entry(buyer).State = EntityState.Modified;
                try
                {
                    await context.SaveChangesAsync();
                }
                catch (DbUpdateConcurrencyException)
                {
                    if (!context.Users.Any(e => e.Id == seller.Id))
                    {
                        return NotFound();
                    }
                    else if (!context.Users.Any(e => e.Id == buyer.Id))
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
