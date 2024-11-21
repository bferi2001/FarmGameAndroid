using FarmGameBackend.CustomExceptions;
using FarmGameBackend.DbContexts;
using FarmGameBackend.Entity;
using Microsoft.AspNetCore.Mvc;
using Microsoft.EntityFrameworkCore;

namespace FarmGameBackend.Helper
{
    public class BarnHelper
    {
        private readonly FarmApplicationContext _context;
        private readonly User _currentUser;
        public BarnHelper(FarmApplicationContext context)
        {
            _context = context;
            //string? userEmail = HttpContext.Items["Email"]?.ToString();
            //_currentUser = _context.GetCurrentUser(userEmail!);
            _currentUser = _context.GetCurrentUser("testemail@gmail.com");
        }

        public List<string> GetUnlockedBarnsNames()
        {
            List<string> unlockedMeatNames;
            try
            {
                unlockedMeatNames = _context.ProductHelper.GetUnlockedMeatNames();
            }
            catch(NotFoundException ex) {
                return new List<string>();
            }
            return _context.BarnTypes.Where( barnType => unlockedMeatNames.Contains(barnType.ProductName)).Select(barnType => barnType.Name).ToList();
        }

        public bool DoesMeattypeExistsAndUnlocked(string _barnName)
        {
            return GetUnlockedBarnsNames().Any(barnName => barnName == _barnName);
        }
        public async Task<Barn?> GetBarnByPosition(int position)
        {
            try
            {
                return await _context.Barns.Where(barn => barn.Position == position && barn.UserName == _currentUser.Email).FirstAsync();
            }
            catch (Exception ex)
            {
                return null;
            }

        }

        public Barn CreateBarn(string _barnTypeName, int _position, int _growTime)
        {
            var r = new Random();
            DateTimeOffset currentTime = DateTimeOffset.Now;
            return new Barn
            {
                UserName = _currentUser.Email,
                TypeName = _barnTypeName,
                Position = _position,
                ProductionStartTime = currentTime,
                ProductionEndTime = currentTime.AddSeconds(_growTime),
                CleaningTime = currentTime.AddSeconds(r.Next(_growTime)),
                FeedingTime = currentTime.AddSeconds(r.Next(_growTime)),
                Level = 0
            };
        }
        public Barn UpdateBarn(Barn barn, int _growTime)
        {
            var r = new Random();
            DateTimeOffset currentTime = DateTimeOffset.Now;
            barn.ProductionStartTime = currentTime;
            barn.ProductionEndTime = currentTime.AddSeconds(_growTime);
            barn.CleaningTime = currentTime.AddSeconds(r.Next(_growTime));
            barn.FeedingTime = currentTime.AddSeconds(r.Next(_growTime));
            return barn;
        }


        public List<string> GetActions(Barn barn)
        {
            List<string> actions = [];
            if (barn.ProductionEndTime != null && barn.ProductionEndTime < DateTimeOffset.Now)
            {
                actions.Add("harvesting");
                return actions;
            }
            if (barn.CleaningTime != null && barn.CleaningTime < DateTimeOffset.Now)
            {
                actions.Add("cleaning");
            }
            if (barn.FeedingTime != null && barn.FeedingTime < DateTimeOffset.Now)
            {
                actions.Add("feeding");
            }
            return actions;
        }

        public async Task UpdateBarnDatabase(Barn barn)
        {
            _context.Entry(barn).State = EntityState.Modified;
            try
            {
                await _context.SaveChangesAsync();
            }
            catch (DbUpdateConcurrencyException)
            {
                if (!_context.ProductHelper.ProductExists(barn.Id))
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
        public async Task UpdateBarnDatabase(Barn barn, User user)
        {
            _context.Entry(barn).State = EntityState.Modified;
            _context.Entry(user).State = EntityState.Modified;
            try
            {
                await _context.SaveChangesAsync();
            }
            catch (DbUpdateConcurrencyException)
            {
                if (!_context.Users.Any(e => e.Id == user.Id))
                {
                    throw new NotFoundException("");
                }
                else if (!_context.Barns.Any(e => e.Id == barn.Id))
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
