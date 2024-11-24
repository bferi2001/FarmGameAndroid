using FarmGameBackend.DbContexts;
using FarmGameBackend.Entity;
using Microsoft.AspNetCore.Mvc;
using FarmGameBackend.CustomExceptions;
using Microsoft.EntityFrameworkCore;


namespace FarmGameBackend.Helper
{
    public class QuestHelper(FarmApplicationContext context)
    {
        public async Task NewQuest(User currentUser)
        {
            List<string> unlockedProducts = await context.ProductHelper.GetUnlockedProductNames();
            Random r = new Random();
            string pickedProduct = unlockedProducts[r.Next(unlockedProducts.Count)];
            int productQuantity = r.Next(1, 51);
            Quest quest = new Quest {
                UserName = currentUser.Email,
                TaskKeyword = "harvest",
                ObjectId = pickedProduct,
                GoalQuantity = productQuantity,
                CurrentQuantity = 0,
                RewardMoney = productQuantity,
                RewardXP = productQuantity
            };
            await PostQuest(quest);
        }

        public async Task ProgressQuest(string taskKeyword, string objectId, int quantity, User currentUser)
        {
            Quest? quest = await GetQuest(taskKeyword, objectId);
            if (quest == null)
            {
                return;
            }
            quest.CurrentQuantity += quantity;
            if (quest.CurrentQuantity >= quest.GoalQuantity)
            {
                User user = context.GetCurrentUser(quest.UserName);
                user.UserMoney += quest.RewardMoney;
                user.UserXP+= quest.RewardXP;
                await context.UserHelper.PutUser(user.Id, user);
                await DeleteQuest(quest.Id);
                await NewQuest(currentUser);
                return;
            }
            await PutQuest(quest.Id, quest!);
        }

        public async Task<List<Quest>> GetQuests(User currentUser)
        {
            return await context.Quests.Where(quest => quest.UserName == currentUser.Email).ToListAsync();
        }
        public async Task<Quest?> GetQuest(int id)
        {
            var quest = await context.Quests.FindAsync(id);

            if (quest == null)
            {
                return null;
            }

            return quest;
        }
        public async Task<Quest?> GetQuest(string taskKeyword, string objectId)
        {
            var quest = await context.Quests.Where(q => q.TaskKeyword == taskKeyword && q.ObjectId == objectId).FirstOrDefaultAsync();

            if (quest == null)
            {
                return null;
            }

            return quest;
        }
        public async Task<Quest?> DeleteQuest(int id)
        {
            var quest = await context.Quests.FindAsync(id);
            if (quest == null)
            {
                throw new NotFoundException("No quest available with current ID");
            }

            context.Quests.Remove(quest);
            await context.SaveChangesAsync();

            return null;
        }
        public async Task<Quest?> PutQuest(int id, Quest quest)
        {
            if (id != quest.Id)
            {
                throw new BadRequestException("");
            }

            context.Entry(quest).State = EntityState.Modified;

            try
            {
                await context.SaveChangesAsync();
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
            context.Quests.Add(quest);
            await context.SaveChangesAsync();

            return quest;
        }
        private bool QuestExists(int id)
        {
            return context.Quests.Any(e => e.Id == id);
        }
    }
}
