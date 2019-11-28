package de.enduni.monsterlair.hazards.view

import de.enduni.monsterlair.hazards.domain.Complexity

data class HazardDisplayModel(
    val id: Long,
    val name: String,
    val url: String,
    val level: Int,
    val complexity: Complexity,
    val source: String
)