namespace FarmGameBackend.Entity
{
    public class Classified
    {
        public int Id { get; set; }
        public int UserId { get; set; }
        public int Price { get; set; }
        public int ProductId { get; set; }
        public DateTime Deadline { get; set; }
        public int Quantity { get; set; }
    }
}
