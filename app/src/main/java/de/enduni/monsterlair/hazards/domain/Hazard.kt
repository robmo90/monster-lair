package de.enduni.monsterlair.hazards.domain

data class Hazard(
    val id: Long,
    val name: String,
    val url: String,
    val level: Int,
    val complexity: Complexity,
    val source: String
)