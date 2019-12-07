package de.enduni.monsterlair.hazards.domain

import de.enduni.monsterlair.common.domain.Complexity

data class Hazard(
    val id: Long,
    val name: String,
    val url: String,
    val level: Int,
    val complexity: Complexity,
    val source: String
)