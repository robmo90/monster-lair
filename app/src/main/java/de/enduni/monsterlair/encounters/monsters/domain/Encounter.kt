package de.enduni.monsterlair.encounters.monsters.domain

import de.enduni.monsterlair.common.EncounterDifficulty

class Encounter(
    val monsters: MutableList<EncounterMonster> = mutableListOf(),
    val level: Int,
    val numberOfPlayers: Int,
    val targetDifficulty: EncounterDifficulty
) {

    fun addMonster(monster: MonsterWithRole) {
        val monsterAlreadyInList = monsters.any { it.id == monster.id }
        if (monsterAlreadyInList) {
            incrementCount(monsterId = monster.id)
        } else {
            monsters.add(
                EncounterMonster(
                    id = monster.id,
                    monster = monster,
                    count = 1
                )
            )
        }
    }

    fun removeMonster(monsterId: Int) {
        monsters.removeIf { it.id == monsterId }
    }

    fun incrementCount(monsterId: Int) {
        monsters.find { it.id == monsterId }
            ?.let { it.count++ }
    }

    fun decrementCount(monsterId: Int) {
        monsters.find { it.id == monsterId }
            ?.let { monster ->
                monster.count--
                if (monster.count == 0) {
                    removeMonster(monsterId)
                }
            }
    }

}

data class EncounterMonster(
    val id: Int,
    val monster: MonsterWithRole,
    var count: Int
)
