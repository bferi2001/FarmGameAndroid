using FarmGameBackend.Entity;

namespace FarmGameBackend.Dao;

public class PlantWithActions
{
    public PlantedPlant Plant { get; set; }
    public List<string> Actions { get; set; }
}