using FarmGameBackend.DbContexts;
using FarmGameBackend.Entity;
using Microsoft.AspNetCore.Mvc;
using FarmGameBackend.CustomExceptions;
using Microsoft.EntityFrameworkCore;


namespace FarmGameBackend.Helper
{
    public class QuestHelper
    {
        private readonly FarmApplicationContext _context;
        private readonly User _currentUser;
        public QuestHelper(FarmApplicationContext context)
        {
            _context = context;
            //string? userEmail = HttpContext.Items["Email"]?.ToString();
            //_currentUser = _context.GetCurrentUser(userEmail!);
            _currentUser = _context.GetCurrentUser("testemail@gmail.com");
        }
        public void NewQuest()
        {

        }
        public async void ProgressQuest(int id, int quantity)
        {
            Quest? quest = await GetQuest(id);
            if(quest != null)
            {
                return;
            }
            quest.CurrentQuantity += quantity;
            if (quest.CurrentQuantity >= quest.GoalQuantity)
            {
                User user = _context.GetCurrentUser(quest.UserName);
                user.UserMoney += quest.RewardMoney;
                user.UserXP+= quest.RewardXP;
                await _context.UserHelper.PutUser(user.Id, user);
                await DeleteQuest(id);
            }
            await PutQuest(id, quest!);
        }
        public async Task<Quest?> GetQuest(int id)
        {
            var quest = await _context.Quests.FindAsync(id);

            if (quest == null)
            {
                return null;
            }

            return quest;
        }
        public async Task<Quest?> DeleteQuest(int id)
        {
            var quest = await _context.Quests.FindAsync(id);
            if (quest == null)
            {
                throw new NotFoundException("");
            }

            _context.Quests.Remove(quest);
            await _context.SaveChangesAsync();

            return null;
        }
        public async Task<Quest?> PutQuest(int id, Quest quest)
        {
            if (id != quest.Id)
            {
                throw new BadRequestException("");
            }

            _context.Entry(quest).State = EntityState.Modified;

            try
            {
                await _context.SaveChangesAsync();
            }
            catch (DbUpdateConcurrencyException)
            {
                if (!QuestExists(id))
                {
                    throw new NotFoundException("");
                }
                else
                {
                    throw;
                }
            }

            return null;
        }

        public async Task<Quest> PostQuest(Quest quest)
        {
            _context.Quests.Add(quest);
            await _context.SaveChangesAsync();

            return quest;
        }
        private bool QuestExists(int id)
        {
            return _context.Quests.Any(e => e.Id == id);
        }
    }
}
