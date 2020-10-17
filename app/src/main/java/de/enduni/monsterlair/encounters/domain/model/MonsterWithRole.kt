package de.enduni.monsterlair.encounters.domain.model

import de.enduni.monsterlair.common.domain.*

data class MonsterWithRole(
    val id: String,
    val name: String,
    val url: String,
    val family: String,
    val level: Int,
    val alignment: Alignment,
    val type: MonsterType,
    val size: Size,
    val rarity: Rarity,
    val traits: List<Trait>,
    val description: String,
    val role: MonsterRole,
    val source: String,
    val sourceType: Source
)