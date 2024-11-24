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
    public class ProductController(FarmApplicationContext context) : Controller
    {
        [HttpGet("currentuser/inventory/market")]
        public async Task<ActionResult<IEnumerable<MarketUserProduct>>> GetUserProductsForMarketAsync()
        {
            List<MarketUserProduct> marketUserProducts = new List<MarketUserProduct>();
            List<UserProduct> userProducts = await GetUserProductsAsync();

            foreach (UserProduct uP in userProducts)
            {
                Product? product = context.Products.FirstOrDefault(productType => productType.Name == uP.ProductName);
                marketUserProducts.Add(new MarketUserProduct
                {
                    ProductName = uP.ProductName,
                    QuickSellPrice = product.QuickSellPrice,
                    Quantity = uP.Quantity
                });
            }
            return marketUserProducts;
        }

        [HttpGet("currentuser/inventory")]
        public async Task<List<UserProduct>> GetUserProductsAsync()
        {
            return await context.ProductHelper.GetUserProductsAsync();
        }
    }
    
}
