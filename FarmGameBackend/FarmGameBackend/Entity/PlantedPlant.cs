namespace FarmGameBackend.Entity
{
    public class PlantedPlant
    {
        public int Id { get; set; }
        public string? UserName { get; set; }
        public required string CropsTypeName { get; set; }
        public required int Position {  get; set; }
        public required DateTimeOffset PlantTime { get; set; }
        public required DateTimeOffset HarvestTime { get; set; }
        public DateTimeOffset? WateringTime { get; set; }
        public DateTimeOffset? WeedingTime { get; set; }
        public DateTimeOffset? FertilisingTime { get; set; }
    }
}
