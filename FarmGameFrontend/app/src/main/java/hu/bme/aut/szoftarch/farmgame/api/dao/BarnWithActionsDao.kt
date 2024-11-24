package hu.bme.aut.szoftarch.farmgame.api.dao

import kotlinx.serialization.Serializable

@Serializable
data class BarnWithActionsDao(
    val barn: BarnDao,
    val actions: Array<String>
)
