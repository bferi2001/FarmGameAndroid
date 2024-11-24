using FarmGameBackend.DbContexts;
using FarmGameBackend.Entity;
using Microsoft.AspNetCore.Mvc;
using FarmGameBackend.Helper;

namespace FarmGameBackend.Controllers;

[Route("api/[controller]")]
[ApiController]
public class FarmController(FarmApplicationContext context) : ControllerBase
{
    User _currentUser = context.GetCurrentUser("testemail@gmail.com");
    
    [HttpGet("size")]
    public ActionResult GetFarmSize()
    {
        int size = FarmSize();
        return Ok(size);
    }

    private int FarmSize()
    {
        var email = _currentUser.Email;
        User user = context.GetCurrentUser(email!);
        int xp = user.UserXP;
        
        int unlockedLandCount = xp/100 + 1;
        return unlockedLandCount;
    }
}