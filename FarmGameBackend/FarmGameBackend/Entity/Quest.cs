namespace FarmGameBackend.Entity
{
    public class Quest
    {
        public int Id { get; set; }
        public required string UserName { get; set; }
        public required string TaskKeyword{ get; set; }
        public required string ObjectId { get; set; }
        public required int GoalQuantity { get; set; }
        public required int CurrentQuantity { get; set; }
        public required int RewardMoney { get; set; }
        public required int RewardXP { get; set; }
    }
}
