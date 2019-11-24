package de.enduni.monsterlair.monsters.view

import de.enduni.monsterlair.monsters.domain.MonsterType

data class MonsterListDisplayModel(
    val name: String,
    val url: String,
    val family: String,
    val level: Int,
    val type: MonsterType
)