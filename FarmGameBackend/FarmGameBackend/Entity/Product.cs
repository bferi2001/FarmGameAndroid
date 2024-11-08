namespace FarmGameBackend.Entity
{
    public class Product
    {
        public int Id { get; set; }
        public required string Name { get; set; }
        public required int QuickSellPrice { get; set; }
        public required int ProductionTimeAsSeconds { get; set; }
        public required int RewardXP { get; set; }
        public required Boolean IsCrop {  get; set; }
    }
}
