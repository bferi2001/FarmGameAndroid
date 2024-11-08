namespace FarmGameBackend.Entity
{
    public class Classified
    {

        public int Id { get; set; }
        public string? UserName { get; set; }
        public required int Price { get; set; }
        public required string ProductName { get; set; }
        public required DateTime Deadline { get; set; }
        public required int Quantity { get; set; }
    }
}
