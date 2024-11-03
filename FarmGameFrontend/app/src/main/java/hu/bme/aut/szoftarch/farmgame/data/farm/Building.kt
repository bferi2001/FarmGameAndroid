package hu.bme.aut.szoftarch.farmgame.data.farm

class Building (
    val id: Int,
    val tag: String,
):Buildable(){
    var level: Int = 0
    val maxLevel: Int = 1

    init {
        //session.getBuildingMaxlevel(tag))
    }

    override fun GetName(): String {
        return "blding $level/$maxLevel"
    }

    override fun GetTag(): String {
        return tag
    }

    override fun getInteractions(): List<String> {
        if(level < maxLevel){
            return listOf("upgrade")
        }
        else{
            return emptyList()
        }
    }

    override fun interact(interaction: String, params: List<String>) {
        if(interaction == "upgrade"){
            level++
        }
    }
}