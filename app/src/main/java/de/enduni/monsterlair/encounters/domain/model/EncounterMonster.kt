package de.enduni.monsterlair.encounters.domain.model

import de.enduni.monsterlair.common.domain.Strength

data class EncounterMonster(
    val id: String,
    val monster: MonsterWithRole,
    val strength: Strength,
    val count: Int
)