namespace FarmGameBackend.Entity
{
    public class Quest
    {
        public int Id { get; set; }
        public required string UserName { get; set; }
        public required int QuestTypeId { get; set; }
    }
}
