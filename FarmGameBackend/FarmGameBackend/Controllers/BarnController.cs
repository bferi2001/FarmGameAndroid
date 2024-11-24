using FarmGameBackend.CustomExceptions;
using FarmGameBackend.DbContexts;
using FarmGameBackend.Entity;
using Microsoft.AspNetCore.Mvc;
using Microsoft.EntityFrameworkCore;

namespace FarmGameBackend.Controllers
{
    [Route("api/farm/barn")]
    public class BarnController : Controller
    {
        private readonly FarmApplicationContext _context;
        private readonly User _currentUser;
        public BarnController(FarmApplicationContext context)
        {
            _context = context;
            //string? userEmail = HttpContext.Items["Email"]?.ToString();
            //_currentUser = _context.GetCurrentUser(userEmail!);
            _currentUser = _context.GetCurrentUser("testemail@gmail.com");
        }
        [HttpGet("{position}/actions")]
        public async Task<ActionResult<IEnumerable<string>>> GetActionsAsync(int position)
        {
            Barn? barnAtPosition = await _context.BarnHelper.GetBarnByPosition(position);
            if (barnAtPosition == null)
            {
                return NotFound();
            }
            return await _context.BarnHelper.GetActions(barnAtPosition);
        }

        [HttpGet("unlocked")]
        public async Task<ActionResult<IEnumerable<string>>> GetUnlockedBarnsNamesAsync()
        {
            
            return _context.BarnHelper.GetUnlockedBarnsNames();
        }

        [HttpPost("{position}/{typeName}")]
        public async Task<ActionResult<Barn>> PostBarnAsync(int position, string typeName)
        {
            if (await _context.BarnHelper.GetBarnByPosition(position) != null)
            {
                return Conflict("The field is not empty");
            }
            string productName = await _context.BarnTypeHelper.GetProductnameByBarntype(typeName);
            Product? productType = await _context.ProductHelper.GetUnlockedProductByName(productName);
            if (productType == null)
            {
                return NotFound();
            }

            DateTimeOffset currentTime = DateTimeOffset.Now;
            int growTime = productType.ProductionTimeAsSeconds;
            Barn newBarn = _context.BarnHelper.CreateBarn(typeName, position, growTime);

            _context.Barns.Add(newBarn);
            await _context.SaveChangesAsync();

            return Ok();
        }

        [HttpPut("{position:int}/cleaning")]
        public async Task<IActionResult> PutCleaning(int position)
        {
            Barn? barnAtPosition = await _context.BarnHelper.GetBarnByPosition(position);
            if (barnAtPosition == null)
            {
                return NotFound();
            }
            if (barnAtPosition.CleaningTime == null)
            {
                return BadRequest("This barn got already cleaned.");
            }
            DateTimeOffset currentTime = DateTimeOffset.Now;
            if (currentTime >= barnAtPosition.CleaningTime && (await _context.BarnHelper.GetActions(barnAtPosition)).Contains("cleaning"))
            {
                barnAtPosition.CleaningTime = null;
                barnAtPosition = _context.BarnHelper.UpdateDateTimes(barnAtPosition);
            }
            else
            {
                return BadRequest("You have to wait...");
            }
            try
            {
                await _context.BarnHelper.UpdateBarnDatabase(barnAtPosition);
            }
            catch (NotFoundException ex)
            {
                return NotFound();
            }
            return NoContent();
        }
        [HttpPut("{position:int}/feeding")]
        public async Task<IActionResult> PutFeeding(int position)
        {
            Barn? barnAtPosition = await _context.BarnHelper.GetBarnByPosition(position);
            if (barnAtPosition == null)
            {
                return NotFound();
            }
            if (barnAtPosition.FeedingTime == null)
            {
                return BadRequest("This barn got already feeded.");
            }
            DateTimeOffset currentTime = DateTimeOffset.Now;
            if (currentTime >= barnAtPosition.FeedingTime && (await _context.BarnHelper.GetActions(barnAtPosition)).Contains("feeding"))
            {
                barnAtPosition.FeedingTime = null;
                barnAtPosition = _context.BarnHelper.UpdateDateTimes(barnAtPosition);
            }
            else
            {
                return BadRequest("You have to wait...");
            }
            try
            {
                await _context.BarnHelper.UpdateBarnDatabase(barnAtPosition);
            }
            catch (NotFoundException ex)
            {
                return NotFound();
            }
            return NoContent();
        }

        [HttpPut("{position:int}/harvesting")]
        public async Task<IActionResult> HarvestBarn(int position)
        {
            Barn? barnAtPosition = await _context.BarnHelper.GetBarnByPosition(position);
            if (barnAtPosition == null)
            {
                return NotFound();
            }
            
            DateTimeOffset currentTime = DateTimeOffset.Now;
            if (!(currentTime >= barnAtPosition.ProductionEndTime && (await _context.BarnHelper.GetActions(barnAtPosition)).Contains("harvesting")))
            {
                return BadRequest("This barn can't be harvested yet.");
            }
            
            try
            {
                var barnProductName = await _context.BarnTypeHelper.GetProductnameByBarntype(barnAtPosition.TypeName);
                var barnProduct = await _context.ProductHelper.GetProductByName(barnProductName);
                if (barnProduct == null)
                {
                    return NotFound();
                }
                var updatedBarn = _context.BarnHelper.UpdateBarn(barnAtPosition, barnProduct.ProductionTimeAsSeconds);
                await _context.BarnHelper.UpdateBarnDatabase(updatedBarn);
                await _context.ProductHelper.AddUserProduct(barnProductName, 3);
                await _context.QuestHelper.ProgressQuest("harvest", barnProductName, 3);
                _currentUser.UserXP = barnProduct.RewardXP;
                await _context.UserHelper.PutUser(_currentUser.Id, _currentUser);
            }
            catch (NotFoundException ex)
            {
                return NotFound(ex.Message);
            }
            catch (BadRequestException ex)
            {
                return BadRequest(ex.Message);
            }
            return NoContent();
        }


        [HttpPut("{position:int}/upgrade")]
        public async Task<IActionResult> UpgradeBarn(int position)
        {
            Barn? barnAtPosition = await _context.BarnHelper.GetBarnByPosition(position);
            if (barnAtPosition == null)
            {
                return NotFound();
            }
            
            try
            {
                var upgradeCost = await _context.BarnTypeHelper.GetBarnTypeCostByLevel(barnAtPosition.TypeName, barnAtPosition.Level+1);
                if(_currentUser.UserMoney < upgradeCost)
                {
                    return BadRequest("Not enough money!");
                }
                _currentUser.UserMoney -= upgradeCost;
                barnAtPosition.Level += 1;
                await _context.BarnHelper.UpdateBarnDatabase(barnAtPosition, _currentUser);
            }
            catch (NotFoundException ex)
            {
                return NotFound(ex.Message);
            }
            catch (BadRequestException ex)
            {
                return BadRequest(ex.Message);
            }
            return Ok();
        }
        
        [HttpGet("barns")]
        public async Task<ActionResult<IEnumerable<Barn>?>> GetBarns()
        {
            return await Helper.Helper.GetBarns(_currentUser.Email, _context);
        }
    }
}
