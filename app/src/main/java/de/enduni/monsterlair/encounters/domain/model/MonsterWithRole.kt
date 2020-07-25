package de.enduni.monsterlair.encounters.domain.model

import de.enduni.monsterlair.common.domain.Alignment
import de.enduni.monsterlair.common.domain.MonsterType
import de.enduni.monsterlair.common.domain.Size

data class MonsterWithRole(
    val id: String,
    val name: String,
    val url: String,
    val family: String,
    val level: Int,
    val alignment: Alignment,
    val type: MonsterType,
    val size: Size,
    val role: MonsterRole,
    val source: String
)