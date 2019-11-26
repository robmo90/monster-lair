package de.enduni.monsterlair.encounters.domain

import de.enduni.monsterlair.monsters.domain.MonsterType

data class MonsterWithRole(
    val id: Long,
    val name: String,
    val url: String,
    val family: String,
    val level: Int,
    val alignment: String,
    val type: MonsterType,
    val size: String,
    val role: CreatureRole
)