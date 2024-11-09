namespace FarmGameBackend.Entity
{
    public class PlantedPlant
    {
        [System.Diagnostics.CodeAnalysis.SetsRequiredMembersAttribute]
        public PlantedPlant(string _cropsTypeName, int _position, int _growTime)
        {
            Random r = new Random();
            DateTimeOffset currentTime = DateTimeOffset.Now;
            //ToDo UserHandling
            UserName = null;
            CropsTypeName = _cropsTypeName;
            Position = _position;
            PlantTime = currentTime;
            HarvestTime = currentTime.AddSeconds(_growTime);
            WateringTime = currentTime.AddSeconds(r.Next(_growTime));
            WeedingTime = currentTime.AddSeconds(r.Next(_growTime));
            FertilisingTime = currentTime.AddSeconds(r.Next(_growTime));
        }

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
