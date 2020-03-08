package de.enduni.monsterlair.monsters.domain

import de.enduni.monsterlair.common.domain.MonsterType

data class Monster(
    val id: Long?,
    val name: String,
    val url: String?,
    val family: String,
    val level: Int,
    val alignment: String?,
    val type: MonsterType,
    val size: String?,
    val source: String
)

