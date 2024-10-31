namespace FarmGameBackend.Entity
{
    public class Product
    {
        public int Id { get; set; }
        public int UserId { get; set; }
        public int QuickSellPrice { get; set; }
        public int ProductionTimeAsSeconds { get; set; }
        public int RewardXP { get; set; }
    }
}
