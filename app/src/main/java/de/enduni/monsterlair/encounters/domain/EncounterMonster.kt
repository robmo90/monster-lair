package de.enduni.monsterlair.encounters.domain

data class EncounterMonster(
    val id: Long,
    val monster: MonsterWithRole,
    var count: Int
)