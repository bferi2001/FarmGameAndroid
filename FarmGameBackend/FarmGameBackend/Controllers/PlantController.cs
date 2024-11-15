using FarmGameBackend.DbContexts;
using FarmGameBackend.Entity;
using Microsoft.AspNetCore.Mvc;
using Microsoft.EntityFrameworkCore;

namespace FarmGameBackend.Controllers
{//ToDo Inventory and UserXP management
 //ToDo Fertilising costs animal poop resource
    [Route("api/farm/plant")]
    public class PlantController : Controller
    {
        private readonly FarmApplicationContext _context;
        private readonly User _currentUser;
        public PlantController(FarmApplicationContext context)
        {
            _context = context;
            string? userEmail = HttpContext.Items["Email"]?.ToString();
            _currentUser = _context.GetCurrentUser(userEmail!);
            /*CurrentUser = new User
            {
                Id = 0,
                Email = "testemail@gmail.com",
                UserXP = 2000,
                UserMoney = 2000
            };*/
        }
        [HttpGet("{position}/actions")]
        public async Task<ActionResult<IEnumerable<string>>> GetActionsAsync(int position)
        {
            PlantedPlant? plantAtPosition = await GetPlantByPosition(position);
            if (plantAtPosition == null)
            {
                return NotFound();
            }
            return GetActions(plantAtPosition);
        }

        [HttpGet("unlocked")]
        public async Task<ActionResult<IEnumerable<string>>> GetUnlockedCropsAsync()
        {
            int userXP = _currentUser.UserXP;
            return await _context.Products.Where(product => product.UnlockXP <= userXP && product.IsCrop)
                                    .Select(product => product.Name)
                                    .ToListAsync(); 
        }

        [HttpPost("{position}/{typeName}")]
        public async Task<ActionResult<PlantedPlant>> PostPlantedPlant(int position, string typeName)
        {
            if (await GetPlantByPosition(position) != null)
            {
                return Conflict("The field is not empty");
            }
            Product? productType = await GetUnlockedProductByName(typeName);
            if (productType == null)
            {
                return NotFound();
            }

            DateTimeOffset currentTime = DateTimeOffset.Now;
            int growTime = productType.ProductionTimeAsSeconds;
            PlantedPlant newPlant = CreatePlantedPlant(typeName, position, growTime);

            _context.PlantedPlants.Add(newPlant);
            await _context.SaveChangesAsync();

            return CreatedAtAction("GetPlantedPlant", new { id = newPlant.Id }, newPlant);
        }

        [HttpPut("{position:int}/watering")]
        public async Task<IActionResult> PutWatering(int position)
        {
            PlantedPlant? plantAtPosition = await GetPlantByPosition(position);
            if (plantAtPosition == null)
            {
                return NotFound();
            }
            if (plantAtPosition.WateringTime == null)
            {
                return BadRequest("This plant got already watered.");
            }
            DateTimeOffset currentTime = DateTimeOffset.Now;
            if (currentTime >= plantAtPosition.WateringTime && GetActions(plantAtPosition).Contains("watering"))
            {
                plantAtPosition.WateringTime = null;
                //ToDo
            }
            return await UpdatePlantedPlantDatabase(plantAtPosition);
        }
        [HttpPut("{position:int}/weeding")]
        public async Task<IActionResult> PutWeeding(int position)
        {
            PlantedPlant? plantAtPosition = await GetPlantByPosition(position);
            if (plantAtPosition == null)
            {
                return NotFound();
            }
            if (plantAtPosition.WeedingTime == null)
            {
                return BadRequest("This plant got already watered.");
            }
            DateTimeOffset currentTime = DateTimeOffset.Now;
            if (currentTime >= plantAtPosition.WeedingTime && GetActions(plantAtPosition).Contains("weeding"))
            {
                plantAtPosition.WeedingTime = null;
                //ToDo
            }
            return await UpdatePlantedPlantDatabase(plantAtPosition);
        }

        [HttpPut("{position:int}/fertilising")]
        public async Task<IActionResult> PutFertilising(int position)
        {
            PlantedPlant? plantAtPosition = await GetPlantByPosition(position);
            if (plantAtPosition == null)
            {
                return NotFound();
            }
            if (plantAtPosition.FertilisingTime == null)
            {
                return BadRequest("This plant got already fertilised.");
            }
            DateTimeOffset currentTime = DateTimeOffset.Now;
            if (currentTime >= plantAtPosition.FertilisingTime && GetActions(plantAtPosition).Contains("fertilising"))
            {
                plantAtPosition.FertilisingTime = null;
                //ToDo Other actions?
            }
            return await UpdatePlantedPlantDatabase(plantAtPosition);
        }

        [HttpDelete("{position:int}/harvest")]
        public async Task<IActionResult> HarvestPlantedPlant(int position)
        {
            PlantedPlant? plantAtPosition = await GetPlantByPosition(position);
            if (plantAtPosition == null)
            {
                return NotFound();
            }
            DateTimeOffset currentTime = DateTimeOffset.Now;
            if (!(currentTime >= plantAtPosition.HarvestTime && GetActions(plantAtPosition).Contains("harvesting")))
            {
                return BadRequest("This plant can't be harvested yet.");
            }
            _context.PlantedPlants.Remove(plantAtPosition);
            await _context.SaveChangesAsync();
            return await AddUserProduct(plantAtPosition.CropsTypeName, 3);
        }
        private async Task<IActionResult> AddUserProduct(string productName, int quantity)
        {
            if (!DoesProductExists(productName))
            {
                return NotFound("Product doesn't exists: " + productName);
            }
            UserProduct? userProduct = await GetUserProduct(productName);
            if ( userProduct == null)
            {
                UserProduct product = new UserProduct
                {
                    ProductName = productName,
                    UserName = _currentUser.Email,
                    Quantity = quantity
                };
                return await PostUserProduct(product);
            }
            else
            {
                userProduct.Quantity += quantity;
                return await PutUserProduct(userProduct.Id, userProduct);
            }
        }

        private async Task<UserProduct?> GetUserProduct(string productName)
        {
            var userProduct =  await _context.UserProduct.Where(userProduct => userProduct.UserName == _currentUser.Email && userProduct.ProductName == productName).FirstOrDefaultAsync();
            if(userProduct == null)
            {
                return null;
            }
            return userProduct;
        }

        private async Task<IActionResult> PostUserProduct(UserProduct userProduct)
        {
            _context.UserProduct.Add(userProduct);
            await _context.SaveChangesAsync();

            return CreatedAtAction("GetUserProduct", new { id = userProduct.Id }, userProduct);

        }

        private async Task<IActionResult> PutUserProduct(int id, UserProduct userProduct)
        {
            if (id != userProduct.Id)
            {
                return BadRequest("Id not match for  modifying "+userProduct.Id);
            }

            _context.Entry(userProduct).State = EntityState.Modified;

            try
            {
                await _context.SaveChangesAsync();
            }
            catch (DbUpdateConcurrencyException)
            {
                if (!UserProductExists(id))
                {
                    return NotFound("UserProduct does not exists with the id: "+id);
                }
                else
                {
                    throw;
                }
            }

            return NoContent();
        }
        private bool UserProductExists(int id)
        {
            return _context.UserProduct.Any(e => e.Id == id);
        }

        private  List<string> GetActions(PlantedPlant plantedPlant)
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

        private async Task<IActionResult> UpdatePlantedPlantDatabase(PlantedPlant updatedPlant)
        {
            _context.Entry(updatedPlant).State = EntityState.Modified;
            try
            {
                await _context.SaveChangesAsync();
            }
            catch (DbUpdateConcurrencyException)
            {
                if (!ProductExists(updatedPlant.Id))
                {
                    return NotFound();
                }
                else
                {
                    throw;
                }
            }
            return NoContent();
        }
        private async Task<Product?> GetUnlockedProductByName(string name)
        {
            if (!DoesCroptypeExistsAndUnlocked(name))
            {
                return null;
            }
            Product? productType = await _context.Products.Where(product => product.Name == name).FirstAsync();
            return productType;
        }

        private Boolean DoesProductExists(string _productName)
        {
            return _context.Products.Any(product => product.Name == _productName);
        }
        private List<string> GetUnlockedCrops()
        {
            int userXP = _currentUser.UserXP;
            return _context.Products.Where(product => product.UnlockXP <= userXP && product.IsCrop)
                                    .Select(product => product.Name)
                                    .ToList();
        }

        private bool DoesCroptypeExistsAndUnlocked(string _cropName)
        {
            return GetUnlockedCrops().Any(cropName => cropName == _cropName);
        }
        private async Task<PlantedPlant?> GetPlantByPosition(int position)
        {
            try
            {
                return await _context.PlantedPlants.Where(plant => plant.Position == position && plant.UserName == _currentUser.Email).FirstAsync();
            }catch (Exception ex)
            {
                return null;
            }
            
        }

        private PlantedPlant CreatePlantedPlant(string _cropsTypeName, int _position, int _growTime)
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
        private bool ProductExists(int id)
        {
            return _context.Products.Any(e => e.Id == id);
        }
    }
}