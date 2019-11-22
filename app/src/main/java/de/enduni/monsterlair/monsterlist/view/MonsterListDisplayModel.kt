package de.enduni.monsterlair.monsterlist.view

import de.enduni.monsterlair.monsterlist.domain.MonsterType

data class MonsterListDisplayModel(
    val name: String,
    val url: String,
    val family: String,
    val level: Int,
    val type: MonsterType
)