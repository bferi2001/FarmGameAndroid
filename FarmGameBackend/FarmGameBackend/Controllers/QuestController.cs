using FarmGameBackend.CustomExceptions;
using FarmGameBackend.DbContexts;
using FarmGameBackend.Entity;
using Microsoft.AspNetCore.Mvc;
using Microsoft.EntityFrameworkCore;

namespace FarmGameBackend.Controllers;

[Route("api/farm/quest")]
[ApiController]
public class QuestController(FarmApplicationContext context) : Controller
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
    
    [HttpGet("availableQuests")]
    public async Task<ActionResult<IEnumerable<Quest>>> GetAvailableQuests()
    {
        return await context.Quests.Where(q => q.UserName == CurrentUser.Email).ToListAsync();
    }
}