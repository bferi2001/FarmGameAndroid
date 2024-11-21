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
    
    [HttpGet("interactions/{landPosition:int}")]
    public async Task<ActionResult> GetInteractions(int landPosition)
    {
        var email = _currentUser.Email;
        User user = context.GetCurrentUser(email!);
        int xp = user.UserXP;
        
        int unlockedLandCount = xp/100 + 1;
        if (landPosition < 0 || landPosition >= unlockedLandCount)
        {
            return BadRequest("Land position is out of range.");
        }
        
        // Check if the land is empty
        PlantedPlant? plant = await Helper.Helper.GetPlantByPosition(landPosition, email, context);
        Barn? barn = await Helper.Helper.GetBarnByPosition(landPosition, email, context);

        if (plant == null && barn == null)
        {
            List<string> interactions = ["Build", "Plough"]; // TODO: store in database
            return Ok(interactions);
        }
        
        return Ok();
    }
    
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