namespace FarmGameBackend.Entity
{
    public class QuestType
    {
        public int Id { get; set; }
        public required string Description { get; set; }
        public required int RewardMoney { get; set; }
        public required int RewardXP { get; set; }
    }
}
