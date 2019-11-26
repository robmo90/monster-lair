package de.enduni.monsterlair.encounters.domain.model

data class EncounterMonster(
    val id: Long,
    val monster: MonsterWithRole,
    var count: Int
)