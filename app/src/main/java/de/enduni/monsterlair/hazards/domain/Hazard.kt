package de.enduni.monsterlair.hazards.domain

import de.enduni.monsterlair.common.domain.Complexity
import de.enduni.monsterlair.common.domain.Rarity
import de.enduni.monsterlair.common.domain.Source

data class Hazard(
    val id: String,
    val name: String,
    val url: String,
    val level: Int,
    val complexity: Complexity,
    val rarity: Rarity,
    val source: String,
    val sourceType: Source,
    val traits: List<String>,
    val description: String
)