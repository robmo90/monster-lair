package de.enduni.monsterlair.encounters.domain.model

data class EncounterHazard(
    val id: String,
    val hazard: HazardWithRole,
    var count: Int
)