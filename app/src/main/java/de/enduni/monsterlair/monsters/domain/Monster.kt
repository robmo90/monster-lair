package de.enduni.monsterlair.monsters.domain

data class Monster(
    val id: Int,
    val name: String,
    val url: String,
    val family: String,
    val level: Int,
    val alignment: String,
    val type: MonsterType,
    val size: String
)

