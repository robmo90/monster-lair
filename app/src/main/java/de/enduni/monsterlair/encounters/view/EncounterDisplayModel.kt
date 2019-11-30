package de.enduni.monsterlair.encounters.view

import de.enduni.monsterlair.encounters.domain.model.EncounterDifficulty

data class EncounterDisplayModel(
    val id: Long,
    val name: String,
    val level: Int,
    val numberOfPlayers: Int,
    val difficulty: EncounterDifficulty,
    val xp: Int,
    val dangers: String
)