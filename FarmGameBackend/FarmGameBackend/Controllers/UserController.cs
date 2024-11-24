using FarmGameBackend.CustomExceptions;
using FarmGameBackend.DbContexts;
using FarmGameBackend.Entity;
using Microsoft.AspNetCore.Mvc;
using Microsoft.EntityFrameworkCore;

namespace FarmGameBackend.Controllers
{
    [Route("api")]
    public class UserController(FarmApplicationContext context) : Controller
    {
        private readonly FarmApplicationContext _context = context;
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

        [HttpGet("currentUser")]
        public async Task<ActionResult<User>> GetCurrentUserAsync()
        {
            return CurrentUser;
        }
    }
}
