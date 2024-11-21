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
        public async Task<ActionResult<IEnumerable<MarketUserProduct>>> GetUserProductsForMarketAsync()
        {
            //ToDo
            return Ok();
        }

        [HttpPost("market")]
        public async Task<ActionResult> PostClassified(/*Todo*/)
        {
            //ToDo
            return Ok();
        }

        [HttpDelete("market/buy/{id}")]
        public async Task<IActionResult> BuyClassified(int íd)
        {
            //ToDo
            return Ok();
        }

        [HttpDelete("market/cancel/{id}")]
        public async Task<IActionResult> CancelClassified(int íd)
        {
            //ToDo
            return Ok();
        }

        [HttpPut("market/quicksell/{productname}/{quantity}")]
        public async Task<IActionResult> QuicksellProduct(string productname, int quantity)
        {
            //ToDo
            return Ok();
        }
        //ToDo szűréses lekérdezés
        //ToDo törlés, ha lejár
    }
}
