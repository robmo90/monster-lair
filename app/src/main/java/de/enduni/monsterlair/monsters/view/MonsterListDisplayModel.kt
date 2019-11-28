package de.enduni.monsterlair.monsters.view

import de.enduni.monsterlair.monsters.domain.MonsterType

data class MonsterListDisplayModel(
    val id: Long,
    val name: String,
    val family: String,
    val level: Int,
    val type: MonsterType
)