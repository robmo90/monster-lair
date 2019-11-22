package de.enduni.monsterlair.monsterlist.domain

data class Monster(
    val name: String,
    val url: String,
    val family: String,
    val level: Int,
    val alignment: String,
    val type: String,
    val size: String
)