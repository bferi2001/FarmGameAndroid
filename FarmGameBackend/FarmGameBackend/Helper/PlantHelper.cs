using FarmGameBackend.CustomExceptions;
using FarmGameBackend.DbContexts;
using FarmGameBackend.Entity;
using Microsoft.AspNetCore.Mvc;
using Microsoft.EntityFrameworkCore;

namespace FarmGameBackend.Helper
{
    public class PlantHelper
    {
        private readonly FarmApplicationContext _context;
        private readonly User _currentUser;
        public PlantHelper(FarmApplicationContext context)
        {
            _context = context;
            //string? userEmail = HttpContext.Items["Email"]?.ToString();
            //_currentUser = _context.GetCurrentUser(userEmail!);
            _currentUser = new User
            {
                Id = 0,
                Email = "testemail@gmail.com",
                UserXP = 2000,
                UserMoney = 2000
            };
        }

        public List<string> GetUnlockedCropsNames()
        {
            int userXP = _currentUser.UserXP;
            return _context.Products.Where(product => product.UnlockXP <= userXP && product.IsCrop)
                                    .Select(product => product.Name)
                                    .ToList();
        }

        public bool DoesCroptypeExistsAndUnlocked(string _cropName)
        {
            return GetUnlockedCropsNames().Any(cropName => cropName == _cropName);
        }
        public async Task<PlantedPlant?> GetPlantByPosition(int position)
        {
            try
            {
                return await _context.PlantedPlants.Where(plant => plant.Position == position && plant.UserName == _currentUser.Email).FirstAsync();
            }
            catch (Exception ex)
            {
                return null;
            }

        }

        public PlantedPlant CreatePlantedPlant(string _cropsTypeName, int _position, int _growTime)
        {
            var r = new Random();
            DateTimeOffset currentTime = DateTimeOffset.Now;
            return new PlantedPlant
            {
                UserName = _currentUser.Email,
                CropsTypeName = _cropsTypeName,
                Position = _position,
                PlantTime = currentTime,
                HarvestTime = currentTime.AddSeconds(_growTime),
                WateringTime = currentTime.AddSeconds(r.Next(_growTime)),
                WeedingTime = currentTime.AddSeconds(r.Next(_growTime)),
                FertilisingTime = currentTime.AddSeconds(r.Next(_growTime))
            };
        }

        public List<string> GetActions(PlantedPlant plantedPlant)
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
            if (plantedPlant.FertilisingTime != null && plantedPlant.FertilisingTime < DateTimeOffset.Now)
            {
                actions.Add("fertilising");
            }
            return actions;
        }

        public async Task UpdatePlantedPlantDatabase(PlantedPlant updatedPlant)
        {
            _context.Entry(updatedPlant).State = EntityState.Modified;
            try
            {
                await _context.SaveChangesAsync();
            }
            catch (DbUpdateConcurrencyException)
            {
                if (!_context.ProductHelper.ProductExists(updatedPlant.Id))
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
    }
}
