using FarmGameBackend.CustomExceptions;
using FarmGameBackend.DbContexts;
using FarmGameBackend.Entity;
using Microsoft.AspNetCore.Mvc;
using FarmGameBackend.Helper;

namespace FarmGameBackend.Controllers;

[Route("api/[controller]")]
[ApiController]
public class FarmController(FarmApplicationContext context) : ControllerBase
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
    
    [HttpGet("size")]
    public ActionResult GetFarmSize()
    {
        int size = FarmSize();
        return Ok(size);
    }

    private int FarmSize()
    {
        int xp = CurrentUser.UserXP;
        
        int unlockedLandCount = xp/100 + 1;
        return unlockedLandCount;
    }
}