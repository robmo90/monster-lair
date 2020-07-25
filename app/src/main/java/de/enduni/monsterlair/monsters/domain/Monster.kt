package de.enduni.monsterlair.monsters.domain

import de.enduni.monsterlair.common.domain.*

data class Monster(
    val id: String,
    val name: String,
    val url: String,
    val family: String,
    val level: Int,
    val alignment: Alignment,
    val type: MonsterType,
    val rarity: Rarity,
    val size: Size,
    val source: String,
    val sourceType: Source,
    val traits: List<String>
)

