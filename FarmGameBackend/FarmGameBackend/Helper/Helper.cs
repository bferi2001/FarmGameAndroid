using FarmGameBackend.DbContexts;
using FarmGameBackend.Entity;
using Microsoft.EntityFrameworkCore;

namespace FarmGameBackend.Helper;

public static class Helper
{
    public static async Task<PlantedPlant?> GetPlantByPosition(int position, string email, FarmApplicationContext context)
    {
        try
        {
            return await context.PlantedPlants.Where(plant => plant.Position == position && plant.UserName == email).FirstAsync();
        }catch (Exception ex)
        {
            return null;
        }
    }
    
    public static async Task<Barn?> GetBarnByPosition(int position, string email, FarmApplicationContext context)
    {
        try
        {
            return await context.Barns.Where(barn => barn.Position == position && barn.UserName == email).FirstAsync();
        }catch (Exception ex)
        {
            return null;
        }
    }
}