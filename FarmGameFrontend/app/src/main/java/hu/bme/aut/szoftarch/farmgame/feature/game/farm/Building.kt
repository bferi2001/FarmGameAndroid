package hu.bme.aut.szoftarch.farmgame.feature.game.farm

import hu.bme.aut.szoftarch.farmgame.view.NameService
import hu.bme.aut.szoftarch.farmgame.view.interaction.MenuLocation
import java.time.Duration
import java.time.LocalDateTime
import java.util.Date

class Building(
    val id: Int,
    private val tag: String,
    var actions: Array<String>,
    val productionStartTime: LocalDateTime,
    val productionEndTime: LocalDateTime,
) : Buildable() {
    var level: Int = 0
    val maxLevel: Int = 3

    var processing: Boolean = false
    var clean: Boolean = true
    override fun isProcessing(): Boolean {
        return true
    }

    override fun getTargetDate(): Date {
        val duration = Duration.between(LocalDateTime.now(), productionEndTime).toMillis()
        val date = Date(Date().toInstant().toEpochMilli() + duration)
        return date
    }

    override fun getStartDate(): Date {
        val date = Date.from(productionStartTime.atZone(java.time.ZoneId.systemDefault()).toInstant())
        return date
    }

    override fun setNewActions(actions: Array<String>) {
        this.actions = actions
    }

    override fun getName(): String {
        return "${NameService.getDisplayName(tag)} lvl${level}"
    }
    override fun getTag(): String {
        return when (level) {
            0 -> {
                tag
            }
            1 -> {
                "$tag:medium"
            }
            else -> {
                "$tag:big"
            }
        }
    }

    override fun getInteractMenu(): MenuLocation {
        return MenuLocation.SIDE
    }

    override fun getInteractions(): List<String> {
        return actions.toList()
    }

    override fun interact(interaction: String, params: List<String>) {
        when (interaction) {
            "start" -> {
                    processing = true
                    clean = false
            }

            "wait" -> {
                if (clean) {
                    processing = false
                } else {
                    clean = false
                }
            }

            "clean" -> {
                clean = true
            }

            "upgrade" -> {
                level++
            }
        }
    }
}