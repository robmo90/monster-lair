package de.enduni.monsterlair.monsters.view

import de.enduni.monsterlair.common.domain.MonsterType

data class MonsterListDisplayModel(
    val id: String,
    val name: String,
    val family: String,
    val level: Int,
    val type: MonsterType,
    val custom: Boolean
)