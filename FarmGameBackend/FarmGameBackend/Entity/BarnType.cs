namespace FarmGameBackend.Entity
{
    public class BarnType
    {
        public int Id { get; set; }
        public required string Name { get; set; }
        public required int FirstUpgradeCost { get; set; }
        public required int SecondUpgradeCost { get; set;}
        public required int ThirdUpgradeCost { get; set; }
        public required string ProductName { get; set; }
    }
}
