namespace FarmGameBackend.Entity
{
    public class Barn
    {
        public int Id { get; set; }
        public string? UserName { get; set; }
        public required string TypeName { get; set; }
        public required int Position { get; set; }
        public required DateTimeOffset ProductionStartTime { get; set; }
        public required DateTimeOffset ProductionEndTime { get; set; }
        public DateTimeOffset? FeedingTime { get; set; }
        public DateTimeOffset? CleaningTime { get; set; }
        public required int Level { get; set; }
    }
}
