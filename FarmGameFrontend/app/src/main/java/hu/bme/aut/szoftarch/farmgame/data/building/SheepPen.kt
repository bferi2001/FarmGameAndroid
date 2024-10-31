package hu.bme.aut.szoftarch.farmgame.data.building

class SheepPen(id: Int, level: Int) : Building(id, level)
{
    override val name: String
        get() = "Sheep Pen"

    override val tag: String
        get() = "building_sheep_pen"

}