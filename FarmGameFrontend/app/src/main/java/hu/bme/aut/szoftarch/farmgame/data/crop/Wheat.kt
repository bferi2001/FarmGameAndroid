package hu.bme.aut.szoftarch.farmgame.data.crop

class Wheat : Crop() {
    override val name: String
        get() = "Wheat"
    override val tag: String
        get() = "crop_wheat"
}