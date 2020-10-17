package de.enduni.monsterlair.encounters.domain.model

import de.enduni.monsterlair.common.domain.Complexity
import de.enduni.monsterlair.common.domain.Rarity
import de.enduni.monsterlair.common.domain.Source
import de.enduni.monsterlair.common.domain.Trait

data class HazardWithRole(
    val id: String,
    val name: String,
    val url: String,
    val level: Int,
    val complexity: Complexity,
    val role: HazardRole,
    val source: String,
    val sourceType: Source,
    val rarity: Rarity,
    val description: String,
    val traits: List<Trait>
)