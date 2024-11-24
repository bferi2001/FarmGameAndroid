using FarmGameBackend.CustomExceptions;
using FarmGameBackend.DbContexts;
using FarmGameBackend.Entity;
using Microsoft.AspNetCore.Mvc;
using Microsoft.EntityFrameworkCore;

namespace FarmGameBackend.Helper
{
    public class BarnHelper(FarmApplicationContext context)
    {
        public List<string> GetUnlockedBarnsNames()
        {
            List<string> unlockedMeatNames;
            try
            {
                unlockedMeatNames = context.ProductHelper.GetUnlockedMeatNames();
            }
            catch(NotFoundException ex) {
                return new List<string>();
            }
            return context.BarnTypes.Where( barnType => unlockedMeatNames.Contains(barnType.ProductName)).Select(barnType => barnType.Name).ToList();
        }

        public bool DoesMeattypeExistsAndUnlocked(string _barnName)
        {
            return GetUnlockedBarnsNames().Any(barnName => barnName == _barnName);
        }
        public async Task<Barn?> GetBarnByPosition(int position, User currentUser)
        {
            try
            {
                return await context.Barns.Where(barn => barn.Position == position && barn.UserName == currentUser.Email).FirstAsync();
            }
            catch (Exception ex)
            {
                return null;
            }

        }

        public Barn CreateBarn(string _barnTypeName, int _position, int _growTime, User currentUser)
        {
            var r = new Random();
            DateTimeOffset currentTime = DateTimeOffset.Now;
            return new Barn
            {
                UserName = currentUser.Email,
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


        public async Task<List<string>> GetActions(Barn barn, User currentUser)
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
            try
            {
                if (await context.BarnTypeHelper.GetBarnTypeCostByLevel(barn.TypeName, barn.Level + 1) <= currentUser.UserMoney)
                {
                    actions.Add("upgrade");
                }
            }
            catch{}
            
            return actions;
        }

        public async Task UpdateBarnDatabase(Barn barn)
        {
            context.Entry(barn).State = EntityState.Modified;
            try
            {
                await context.SaveChangesAsync();
            }
            catch (DbUpdateConcurrencyException)
            {
                if (!context.ProductHelper.ProductExists(barn.Id))
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
            context.Entry(barn).State = EntityState.Modified;
            context.Entry(user).State = EntityState.Modified;
            try
            {
                await context.SaveChangesAsync();
            }
            catch (DbUpdateConcurrencyException)
            {
                if (!context.Users.Any(e => e.Id == user.Id))
                {
                    throw new NotFoundException("");
                }
                else if (!context.Barns.Any(e => e.Id == barn.Id))
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
        public Barn UpdateDateTimes(Barn updatedBarn)
        {
            if (updatedBarn.ProductionEndTime == null)
            {
                updatedBarn.ProductionEndTime = DateTimeOffset.Now + (DateTimeOffset.Now - updatedBarn.ProductionEndTime) * 0.9;
            }
            if (updatedBarn.CleaningTime == null)
            {
                updatedBarn.CleaningTime = DateTimeOffset.Now + (DateTimeOffset.Now - updatedBarn.CleaningTime) * 0.9;
            }
            if (updatedBarn.FeedingTime == null)
            {
                updatedBarn.FeedingTime = DateTimeOffset.Now + (DateTimeOffset.Now - updatedBarn.FeedingTime) * 0.9;
            }
            return updatedBarn;
        }

    }
}
