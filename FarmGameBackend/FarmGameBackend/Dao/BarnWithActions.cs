using FarmGameBackend.Entity;

namespace FarmGameBackend.Dao;

public class BarnWithActions
{
    public Barn Barn { get; set; }
    public List<String> Actions { get; set; }
}