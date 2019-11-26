package de.enduni.monsterlair.encounters.domain.model

data class EncounterBudget(
    val currentBudget: Int,
    val targetBudget: Int,
    val currentDifficulty: EncounterDifficulty
)