package hu.bme.aut.szoftarch.farmgame.feature.quests

import hu.bme.aut.szoftarch.farmgame.api.DummyController
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

class QuestViewModel {
    private val controller = DummyController()
    private val _quests = MutableStateFlow(loadQuests())
    val quests: StateFlow<MutableList<Quest>> = _quests.asStateFlow()

    fun claimQuest(quest: Quest) {
        controller.claimQuest(quest)
        _quests.value = loadQuests()
    }

    fun loadQuests(): MutableList<Quest> {
        return controller.getQuests().toMutableList()
    }
}


