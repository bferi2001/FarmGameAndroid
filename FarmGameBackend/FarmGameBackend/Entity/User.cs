namespace FarmGameBackend.Entity
{
    public class User
    {
        public int Id { get; set; }
        public required string Email { get; set; }
        public required int UserXP { get; set; }
        public required int UserMoney { get; set; }
    }
}
