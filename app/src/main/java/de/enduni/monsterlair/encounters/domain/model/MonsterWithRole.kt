package de.enduni.monsterlair.encounters.domain.model

import de.enduni.monsterlair.common.domain.MonsterType

data class MonsterWithRole(
    val id: Long,
    val name: String,
    val url: String?,
    val family: String,
    val level: Int,
    val alignment: String?,
    val type: MonsterType,
    val size: String?,
    val role: MonsterRole,
    val source: String
)