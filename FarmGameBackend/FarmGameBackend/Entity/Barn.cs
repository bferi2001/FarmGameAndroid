namespace FarmGameBackend.Entity
{
    public class Barn
    {
        public int Id { get; set; }
        public int UserId { get; set; }
        public int TypeId { get; set; }
        public DateTimeOffset ProductionStartTime { get; set; }
        public DateTimeOffset ProductionEndTime { get; set; }
        public DateTimeOffset FeedingTime { get; set; }
        public DateTimeOffset CleaningTime { get; set; }
    }
}
