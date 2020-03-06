package de.enduni.monsterlair.encounters.domain.model

import java.io.Serializable

enum class EncounterDifficulty(
    val budget: Int,
    val characterAdjustment: Int
) : Serializable {
    TRIVIAL(40, 10),
    LOW(60, 15),
    MODERATE(80, 20),
    SEVERE(120, 30),
    EXTREME(160, 40)
}