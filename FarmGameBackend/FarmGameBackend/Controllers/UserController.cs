using FarmGameBackend.DbContexts;
using FarmGameBackend.Entity;
using Microsoft.AspNetCore.Mvc;
using Microsoft.EntityFrameworkCore;

namespace FarmGameBackend.Controllers
{
    [Route("api")]
    public class UserController : Controller
    {
        private readonly FarmApplicationContext _context;
        private readonly User _currentUser;
        public UserController(FarmApplicationContext context)
        {
            _context = context;
            //string? userEmail = HttpContext.Items["Email"]?.ToString();
            //_currentUser = _context.GetCurrentUser(userEmail!);
            _currentUser = _context.GetCurrentUser("testemail@gmail.com");
        }
        [HttpGet("currentUser")]
        public async Task<ActionResult<User>> GetCurrentUserAsync()
        {
            return _currentUser;
        }
    }
}
