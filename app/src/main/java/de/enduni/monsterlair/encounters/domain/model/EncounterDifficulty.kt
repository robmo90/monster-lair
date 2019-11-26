package de.enduni.monsterlair.encounters.domain.model

enum class EncounterDifficulty(
    val budget: Int,
    val characterAdjustment: Int
) {
    TRIVIAL(40, 10),
    LOW(60, 15),
    MODERATE(80, 20),
    SEVERE(120, 30),
    EXTREME(160, 40)
}