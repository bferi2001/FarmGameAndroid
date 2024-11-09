using FarmGameBackend.Entity;
using Microsoft.EntityFrameworkCore;

namespace FarmGameBackend.DbContexts
{
    public class FarmApplicationContext : DbContext
    {
        public FarmApplicationContext()
        {
        }

        public FarmApplicationContext(DbContextOptions<FarmApplicationContext> options)
            : base(options)
        {
        }
        public DbSet<Barn> Barns { get; set; }
        public DbSet<BarnType> BarnTypes { get; set; }
        public DbSet<Classified> Classifieds { get; set; }
        public DbSet<PlantedPlant> PlantedPlants { get; set; }
        public DbSet<Product> Products { get; set; }
        public DbSet<Quest> Quests { get; set; }
        public DbSet<QuestType> QuestTypes { get; set; }
        public DbSet<User> Users { get; set; }
        public DbSet<FarmGameBackend.Entity.UserProduct> UserProduct { get; set; } = default!;

        public User GetCurrentUser()
        {
            //Dummy
            return new User
            {
                Id = 0,
                Email = "testemail@gmail.com",
                UserXP = 2000,
                UserMoney = 2000
            };
        }
    }
}
