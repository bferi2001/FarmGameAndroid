using FarmGameBackend.DbContexts;
using FarmGameBackend.Entity;
using Microsoft.AspNetCore.Http;
using Microsoft.AspNetCore.Mvc;
using Microsoft.EntityFrameworkCore;
using static FarmGameBackend.Helper.ProductHelper;

namespace FarmGameBackend.Controllers
{
    [Route("api/farm")]
    public class ProductController : Controller
    {
        private readonly FarmApplicationContext _context;
        private readonly User _currentUser;
        public ProductController(FarmApplicationContext context)
        {
            _context = context;
            //string? userEmail = HttpContext.Items["Email"]?.ToString();
            //_currentUser = _context.GetCurrentUser(userEmail!);
            _currentUser = new User
            {
                Id = 0,
                Email = "testemail@gmail.com",
                UserXP = 2000,
                UserMoney = 2000
            };
        }
        [HttpGet("currentuser/inventory/market")]
        public async Task<ActionResult<IEnumerable<MarketUserProduct>>> GetUserProductsForMarketAsync()
        {
            List<MarketUserProduct> marketUserProducts = new List<MarketUserProduct>();
            List<UserProduct> userProducts = await GetUserProductsAsync();

            foreach (UserProduct uP in userProducts)
            {
                Product product = _context.Products.FirstOrDefault(productType => productType.Name == uP.ProductName);
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
            return await _context.ProductHelper.GetUserProductsAsync();
        }
    }
    
}
