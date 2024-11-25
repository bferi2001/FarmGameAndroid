using FarmGameBackend.CustomExceptions;
using FarmGameBackend.DbContexts;
using FarmGameBackend.Entity;
using Microsoft.AspNetCore.Mvc;
using Microsoft.EntityFrameworkCore;

namespace FarmGameBackend.Helper
{
    public class PlantHelper(FarmApplicationContext context)
    {
        public List<string> GetUnlockedCropsNames(User currentUser)
        {
            int userXP = currentUser.UserXP;
            return context.Products.Where(product => product.UnlockXP <= userXP && product.IsCrop)
                                    .Select(product => product.Name)
                                    .ToList();
        }

        public bool DoesCroptypeExistsAndUnlocked(string _cropName, User currentUser)
        {
            return GetUnlockedCropsNames(currentUser).Any(cropName => cropName == _cropName);
        }
        public async Task<PlantedPlant?> GetPlantByPosition(int position, User currentUser)
        {
            try
            {
                return await context.PlantedPlants.Where(plant => plant.Position == position && plant.UserName == currentUser.Email).FirstAsync();
            }
            catch (Exception ex)
            {
                return null;
            }

        }

        public PlantedPlant CreatePlantedPlant(string _cropsTypeName, int _position, int _growTime, User currentUser)
        {
            var r = new Random();
            DateTimeOffset currentTime = DateTimeOffset.Now;
            return new PlantedPlant
            {
                UserName = currentUser.Email,
                CropsTypeName = _cropsTypeName,
                Position = _position,
                PlantTime = currentTime,
                HarvestTime = currentTime.AddSeconds(_growTime),
                WateringTime = currentTime.AddSeconds(r.Next(_growTime)),
                WeedingTime = currentTime.AddSeconds(r.Next(_growTime)),
                FertilisingTime = currentTime.AddSeconds(r.Next(_growTime))
            };
        }

        public async Task<List<string>> GetActions(PlantedPlant plantedPlant, User currentUser)
        {
            List<string> actions = [];
            if (plantedPlant.HarvestTime != null && plantedPlant.HarvestTime < DateTimeOffset.Now)
            {
                actions.Add("harvesting");
                return actions;
            }
            if (plantedPlant.WateringTime != null && plantedPlant.WateringTime < DateTimeOffset.Now)
            {
                actions.Add("watering");
            }
            if (plantedPlant.WeedingTime != null && plantedPlant.WeedingTime < DateTimeOffset.Now)
            {
                actions.Add("weeding");
            }
            UserProduct? manure = await context.ProductHelper.GetUserProduct("other_manure", currentUser);
            if (plantedPlant.FertilisingTime != null && plantedPlant.FertilisingTime < DateTimeOffset.Now && manure != null && manure.Quantity > 0)
            {
                actions.Add("fertilising");
            }
            return actions;
        }

        public async Task UpdatePlantedPlantDatabase(PlantedPlant updatedPlant)
        {
            context.Entry(updatedPlant).State = EntityState.Modified;
            try
            {
                await context.SaveChangesAsync();
            }
            catch (DbUpdateConcurrencyException)
            {
                if (!context.ProductHelper.ProductExists(updatedPlant.Id))
                {
                    throw new NotFoundException("");
                }
                else
                {
                    throw;
                }
            }
            return;
        }

        public PlantedPlant UpdateDateTimes(PlantedPlant updatedPlant)
        {
            if(updatedPlant.WateringTime == null)
            {
                updatedPlant.WateringTime = DateTimeOffset.Now + (DateTimeOffset.Now-updatedPlant.WateringTime)*0.9;
            }
            if (updatedPlant.FertilisingTime == null)
            {
                updatedPlant.FertilisingTime = DateTimeOffset.Now + (DateTimeOffset.Now - updatedPlant.FertilisingTime) * 0.9;
            }
            if (updatedPlant.WeedingTime == null)
            {
                updatedPlant.WeedingTime = DateTimeOffset.Now + (DateTimeOffset.Now - updatedPlant.WeedingTime) * 0.9;
            }
            if (updatedPlant.HarvestTime == null)
            {
                updatedPlant.HarvestTime = DateTimeOffset.Now + (DateTimeOffset.Now - updatedPlant.HarvestTime) * 0.9;
            }
            return updatedPlant;
        }
    }
}
