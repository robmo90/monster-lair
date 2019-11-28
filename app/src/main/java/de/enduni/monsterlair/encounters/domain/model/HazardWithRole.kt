package de.enduni.monsterlair.encounters.domain.model

import de.enduni.monsterlair.hazards.domain.Complexity

data class HazardWithRole(
    val id: Long,
    val name: String,
    val url: String,
    val level: Int,
    val complexity: Complexity,
    val role: HazardRole,
    val source: String
)