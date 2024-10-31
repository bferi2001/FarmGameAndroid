package hu.bme.aut.szoftarch.farmgame.data.crop

class Flowers : Crop() {
    override val name: String
        get() = "Flowers"
    override val tag: String
        get() = "crop_flowers"
}