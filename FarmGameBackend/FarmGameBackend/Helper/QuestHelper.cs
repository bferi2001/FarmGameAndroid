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
        public async Task NewQuest()
        {
            List<string> unlockedProducts = await _context.ProductHelper.GetUnlockedProductNames();
            Random r = new Random();
            string pickedProduct = unlockedProducts[r.Next(unlockedProducts.Count)];
            int productQuantity = r.Next(1, 51);
            Quest quest = new Quest {
                UserName = _currentUser.Email,
                TaskKeyword = "harvest",
                ObjectId = pickedProduct,
                GoalQuantity = productQuantity,
                CurrentQuantity = 0,
                RewardMoney = productQuantity,
                RewardXP = productQuantity
            };
        }

        public async Task ProgressQuest(string taskKeyword, string objectId, int quantity)
        {
            Quest? quest = await GetQuest(taskKeyword, objectId);
            if (quest == null)
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
                await DeleteQuest(quest.Id);
                await NewQuest();
            }
            await PutQuest(quest.Id, quest!);
        }

        public async Task<List<Quest>> GetQuests()
        {
            return await _context.Quests.Where(quest => quest.UserName == _currentUser.Email).ToListAsync();
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
        public async Task<Quest?> GetQuest(string taskKeyword, string objectId)
        {
            var quest = await _context.Quests.Where(q => q.TaskKeyword == taskKeyword && q.ObjectId == objectId).FirstOrDefaultAsync();

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
