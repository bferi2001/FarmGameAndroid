namespace FarmGameBackend.Entity
{
    public class UserProduct
    {
        public int Id { get; set; }
        public required string ProductName { get; set; }
        public required string UserName { get; set; }
        public required int Quantity { get; set; }
    }
}
