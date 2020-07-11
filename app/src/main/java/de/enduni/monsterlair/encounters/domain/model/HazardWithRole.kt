package de.enduni.monsterlair.encounters.domain.model

import de.enduni.monsterlair.common.domain.Complexity

data class HazardWithRole(
    val id: String,
    val name: String,
    val url: String,
    val level: Int,
    val complexity: Complexity,
    val role: HazardRole,
    val source: String
)