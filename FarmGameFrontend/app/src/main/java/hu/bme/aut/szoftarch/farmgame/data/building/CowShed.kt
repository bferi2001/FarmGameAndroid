package hu.bme.aut.szoftarch.farmgame.data.building

class CowShed(id: Int, level: Int) : Building(id, level)
{
    override val name: String
        get() = "Cow Shed"

    override val tag: String
        get() = "building_cow_shed"

}