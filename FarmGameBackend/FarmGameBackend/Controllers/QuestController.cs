using FarmGameBackend.DbContexts;
using FarmGameBackend.Entity;
using Microsoft.AspNetCore.Mvc;
using Microsoft.EntityFrameworkCore;

namespace FarmGameBackend.Controllers;

[Route("api/farm/quest")]
[ApiController]
public class QuestController(FarmApplicationContext context) : Controller
{
    private readonly User _currentUser = context.GetCurrentUser("testemail@gmail.com");
    //string? userEmail = HttpContext.Items["Email"]?.ToString();
    //_currentUser = _context.GetCurrentUser(userEmail!);
    
    [HttpGet("availableQuests")]
    public async Task<ActionResult<IEnumerable<QuestType>>> GetAvailableQuests()
    {
        return await context.QuestTypes.ToListAsync();
    }
}