package de.enduni.monsterlair.encounters.domain.model

data class EncounterHazard(
    val id: Long,
    val hazard: HazardWithRole,
    var count: Int
)