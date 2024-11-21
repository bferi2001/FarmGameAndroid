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
            _currentUser = new User
            {
                Id = 0,
                Email = "testemail@gmail.com",
                UserXP = 2000,
                UserMoney = 2000
            };
        }
        [HttpGet("{position}/actions")]
        public async Task<ActionResult<IEnumerable<string>>> GetActionsAsync(int position)
        {
            Barn? barnAtPosition = await _context.BarnHelper.GetBarnByPosition(position);
            if (barnAtPosition == null)
            {
                return NotFound();
            }
            return _context.BarnHelper.GetActions(barnAtPosition);
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
            Product? productType = await _context.ProductHelper.GetUnlockedProductByName(typeName);
            if (productType == null)
            {
                return NotFound();
            }

            DateTimeOffset currentTime = DateTimeOffset.Now;
            int growTime = productType.ProductionTimeAsSeconds;
            Barn newBarn = _context.BarnHelper.CreateBarn(typeName, position, growTime);

            _context.Barns.Add(newBarn);
            await _context.SaveChangesAsync();

            return CreatedAtAction("GetBarn", new { id = newBarn.Id }, newBarn);
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
            if (currentTime >= barnAtPosition.CleaningTime && _context.BarnHelper.GetActions(barnAtPosition).Contains("cleaning"))
            {
                barnAtPosition.CleaningTime = null;
                //ToDo
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
            if (currentTime >= barnAtPosition.FeedingTime && _context.BarnHelper.GetActions(barnAtPosition).Contains("feeding"))
            {
                barnAtPosition.FeedingTime = null;
                //ToDo
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

        [HttpPut("{position:int}/harvest")]
        public async Task<IActionResult> HarvestBarn(int position)
        {
            Barn? barnAtPosition = await _context.BarnHelper.GetBarnByPosition(position);
            if (barnAtPosition == null)
            {
                return NotFound();
            }
            DateTimeOffset currentTime = DateTimeOffset.Now;
            if (!(currentTime >= barnAtPosition.ProductionEndTime && _context.BarnHelper.GetActions(barnAtPosition).Contains("harvesting")))
            {
                return BadRequest("This barn can't be harvested yet.");
            }
            await _context.SaveChangesAsync();
            try
            {
                var barnProduct = await _context.BarnHelper.GetProductnameByBarntype(barnAtPosition.TypeName);
                await _context.ProductHelper.AddUserProduct(barnProduct, 3);
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

    }
}
