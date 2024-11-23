package hu.bme.aut.szoftarch.farmgame.feature.game.farm

import hu.bme.aut.szoftarch.farmgame.view.NameService
import hu.bme.aut.szoftarch.farmgame.view.interaction.MenuLocation
import java.time.Duration
import java.time.LocalDateTime
import java.util.Date

class Planter(
    val id: Int,
    val plantTime: LocalDateTime,
    var actions: Array<String>,
    val harvestTime: LocalDateTime? = null,
) : Buildable() {
    var content: Crop? = null
    var finished: Boolean = false
    var water: Boolean = false
    var fertilizer: Boolean = false

    override fun isProcessing(): Boolean {
        return content != null
    }

    override fun getTargetDate(): Date {
        if(harvestTime==null) return Date()
        val duration = Duration.between(LocalDateTime.now(), harvestTime).toMillis()
        val date = Date(Date().toInstant().toEpochMilli() + duration)
        return date
    }

    override fun getStartDate(): Date {
        val date = Date.from(plantTime.atZone(java.time.ZoneId.systemDefault()).toInstant())
        return date
    }

    override fun setNewActions(actions: Array<String>) {
        this.actions = actions
    }

    var cleaned: Boolean = true

    var progress: Int = 0

    override fun getName(): String {
        return content?.name ?: "Farm Land"
    }

    override fun getTag(): String {
        return content?.tag ?: "crop_null"
    }

    override fun getInteractMenu(): MenuLocation {
        return MenuLocation.SIDE
    }

    override fun getInteractions(): List<String> {
        return actions.toList()
    }

    override fun interact(interaction: String, params: List<String>) {
        when (interaction) {
            "wait" -> {
                progress++
            }

            "water" -> {
                water = true
                progress += 2
            }

            "fertilize" -> {
                fertilizer = true
                progress += 5
            }

            "clean" -> {
                cleaned = true
            }

            "collect" -> {
                content = null
                finished = false
                progress = 0
            }

            else -> {
                plant(Crop(NameService.getDisplayName(interaction), interaction))
            }
        }
        if (progress >= 10) {
            finished = true
        }
    }

    fun plant(crop: Crop) {
        if (content != null) {
            throw Exception("Planter is not empty")
        }
        content = crop
        finished = false
        progress = 0
    }
}