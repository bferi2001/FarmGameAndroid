using FarmGameBackend.CustomExceptions;
using FarmGameBackend.Dao;
using FarmGameBackend.DbContexts;
using FarmGameBackend.Entity;
using Microsoft.AspNetCore.Mvc;
using Microsoft.EntityFrameworkCore;

namespace FarmGameBackend.Controllers
{
    [Route("api/farm/barn")]
    public class BarnController(FarmApplicationContext context) : Controller
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

        [HttpGet("{position}/actions")]
        public async Task<ActionResult<IEnumerable<string>>> GetActionsAsync(int position)
        {
            Barn? barnAtPosition = await context.BarnHelper.GetBarnByPosition(position, CurrentUser);
            if (barnAtPosition == null)
            {
                return NotFound();
            }
            return await context.BarnHelper.GetActions(barnAtPosition, CurrentUser);
        }

        [HttpGet("unlocked")]
        public async Task<ActionResult<IEnumerable<string>>> GetUnlockedBarnsNamesAsync()
        {
            
            return context.BarnHelper.GetUnlockedBarnsNames();
        }

        [HttpPost("{position}/{typeName}")]
        public async Task<ActionResult<Barn>> PostBarnAsync(int position, string typeName)
        {
            if (await context.BarnHelper.GetBarnByPosition(position, CurrentUser) != null)
            {
                return Conflict("The field is not empty");
            }
            string productName = await context.BarnTypeHelper.GetProductnameByBarntype(typeName);
            Product? productType = await context.ProductHelper.GetUnlockedProductByName(productName);
            if (productType == null)
            {
                return NotFound();
            }

            DateTimeOffset currentTime = DateTimeOffset.Now;
            int growTime = productType.ProductionTimeAsSeconds;
            Barn newBarn = context.BarnHelper.CreateBarn(typeName, position, growTime, CurrentUser);

            context.Barns.Add(newBarn);
            await context.SaveChangesAsync();

            return Ok();
        }

        [HttpPut("{position:int}/cleaning")]
        public async Task<IActionResult> PutCleaning(int position)
        {
            Barn? barnAtPosition = await context.BarnHelper.GetBarnByPosition(position, CurrentUser);
            if (barnAtPosition == null)
            {
                return NotFound();
            }
            if (barnAtPosition.CleaningTime == null)
            {
                return BadRequest("This barn got already cleaned.");
            }
            DateTimeOffset currentTime = DateTimeOffset.Now;
            if (currentTime >= barnAtPosition.CleaningTime && (await context.BarnHelper.GetActions(barnAtPosition, CurrentUser)).Contains("cleaning"))
            {
                barnAtPosition.CleaningTime = null;
                barnAtPosition = context.BarnHelper.UpdateDateTimes(barnAtPosition);
                await context.ProductHelper.AddUserProduct("other_manure", 3);
            }
            else
            {
                return BadRequest("You have to wait...");
            }
            try
            {
                await context.BarnHelper.UpdateBarnDatabase(barnAtPosition);
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
            Barn? barnAtPosition = await context.BarnHelper.GetBarnByPosition(position, CurrentUser);
            if (barnAtPosition == null)
            {
                return NotFound();
            }
            if (barnAtPosition.FeedingTime == null)
            {
                return BadRequest("This barn got already feeded.");
            }
            DateTimeOffset currentTime = DateTimeOffset.Now;
            if (currentTime >= barnAtPosition.FeedingTime && (await context.BarnHelper.GetActions(barnAtPosition, CurrentUser)).Contains("feeding"))
            {
                barnAtPosition.FeedingTime = null;
                barnAtPosition = context.BarnHelper.UpdateDateTimes(barnAtPosition);
            }
            else
            {
                return BadRequest("You have to wait...");
            }
            try
            {
                await context.BarnHelper.UpdateBarnDatabase(barnAtPosition);
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
            Barn? barnAtPosition = await context.BarnHelper.GetBarnByPosition(position, CurrentUser);
            if (barnAtPosition == null)
            {
                return NotFound();
            }
            
            DateTimeOffset currentTime = DateTimeOffset.Now;
            if (!(currentTime >= barnAtPosition.ProductionEndTime && (await context.BarnHelper.GetActions(barnAtPosition, CurrentUser)).Contains("harvesting")))
            {
                return BadRequest("This barn can't be harvested yet.");
            }
            
            try
            {
                var barnProductName = await context.BarnTypeHelper.GetProductnameByBarntype(barnAtPosition.TypeName);
                var barnProduct = await context.ProductHelper.GetProductByName(barnProductName);
                if (barnProduct == null)
                {
                    return NotFound();
                }
                var updatedBarn = context.BarnHelper.UpdateBarn(barnAtPosition, barnProduct.ProductionTimeAsSeconds);
                await context.BarnHelper.UpdateBarnDatabase(updatedBarn);
                await context.ProductHelper.AddUserProduct(barnProductName, 3);
                await context.QuestHelper.ProgressQuest("harvest", barnProductName, 3, CurrentUser);
                CurrentUser.UserXP += barnProduct.RewardXP;
                await context.UserHelper.PutUser(CurrentUser.Id, CurrentUser);
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
            Barn? barnAtPosition = await context.BarnHelper.GetBarnByPosition(position, CurrentUser);
            if (barnAtPosition == null)
            {
                return NotFound();
            }
            
            try
            {
                var upgradeCost = await context.BarnTypeHelper.GetBarnTypeCostByLevel(barnAtPosition.TypeName, barnAtPosition.Level+1);
                if(CurrentUser.UserMoney < upgradeCost)
                {
                    return BadRequest("Not enough money!");
                }
                CurrentUser.UserMoney -= upgradeCost;
                barnAtPosition.Level += 1;
                await context.BarnHelper.UpdateBarnDatabase(barnAtPosition, CurrentUser);
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
        public async Task<ActionResult<IEnumerable<BarnWithActions>?>> GetBarns()
        {
            var barns = await Helper.Helper.GetBarns(CurrentUser.Email, context);
            var barnsWithActions = new List<BarnWithActions>();
            if (barns == null) return Ok(barnsWithActions);
            
            foreach (Barn barn in barns)
            {
                var actions = await context.BarnHelper.GetActions(barn, CurrentUser);
                var barnWithActions = new BarnWithActions
                {
                    Barn = barn,
                    Actions = actions
                };
                barnsWithActions.Add(barnWithActions);
            }

            return Ok(barnsWithActions);
        }
    }
}
