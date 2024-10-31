namespace FarmGameBackend.Entity
{
    public class PlantedPlant
    {
        public int Id { get; set; }
        public int UserId { get; set; }
        public int TypeId { get; set; }
        public DateTimeOffset PlantTime { get; set; }
        public DateTimeOffset HarvestTime { get; set; }
        public DateTimeOffset WateringTime { get; set; }
        public DateTimeOffset WeedingTime { get; set; }
        public DateTimeOffset FertilisingTime { get; set; }

    }
}
