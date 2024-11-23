using FarmGameBackend.CustomExceptions;
using FarmGameBackend.Entity;
using FarmGameBackend.Helper;
using Microsoft.EntityFrameworkCore;
using System.Security.Cryptography.X509Certificates;

namespace FarmGameBackend.DbContexts
{
    public class FarmApplicationContext : DbContext
    {
        public ProductHelper ProductHelper;
        public PlantHelper PlantHelper;
        public BarnHelper BarnHelper;
        public BarnTypeHelper BarnTypeHelper;
        public QuestHelper QuestHelper;
        public UserHelper UserHelper;
        public FarmApplicationContext()
        {
            ProductHelper= new ProductHelper(this);
            PlantHelper = new PlantHelper(this);
            BarnHelper = new BarnHelper(this);
            BarnTypeHelper = new BarnTypeHelper(this);
            QuestHelper = new QuestHelper(this)
            UserHelper = new UserHelper(this);
        }

        public FarmApplicationContext(DbContextOptions<FarmApplicationContext> options)
            : base(options)
        {
            ProductHelper = new ProductHelper(this);
            PlantHelper = new PlantHelper(this);
            BarnHelper = new BarnHelper(this);
            BarnTypeHelper = new BarnTypeHelper(this);
            QuestHelper = new QuestHelper(this);
            UserHelper = new UserHelper(this);
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

        public User GetCurrentUser(string email)
        {
            User? user = Users.FirstOrDefault(u => u.Email == email);
            if (user != null) return user;
            
            // If user does not exist, create a new user
            user = new User{ Email = email, UserXP = 0, UserMoney = 0};
            Users.Add(user);
            SaveChanges();
            return user;
        }
    }
}
