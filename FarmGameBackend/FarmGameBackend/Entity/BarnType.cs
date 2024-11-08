namespace FarmGameBackend.Entity
{
    public class BarnType
    {
        public int Id { get; set; }
        public string Name { get; set; }
        public int FirstUpgradeCost { get; set; }
        public int SecondUpgradeCost { get; set;}
        public int ThirdUpgradeCost { get; set; }
        public int ProductId { get; set; }
    }
}
