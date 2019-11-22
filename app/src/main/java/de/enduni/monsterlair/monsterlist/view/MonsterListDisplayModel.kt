package de.enduni.monsterlair.monsterlist.view

data class MonsterListDisplayModel(
    val name: String,
    val url: String,
    val family: String,
    val level: Int,
    val type: String
)