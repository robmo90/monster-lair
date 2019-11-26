package de.enduni.monsterlair.encounters.domain

class Encounter(
    val id: Long? = null,
    var name: String = "Encounter",
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

    fun removeMonster(monsterId: Long) {
        monsters.removeIf { it.id == monsterId }
    }

    fun incrementCount(monsterId: Long) {
        monsters.find { it.id == monsterId }
            ?.let { it.count++ }
    }

    fun decrementCount(monsterId: Long) {
        monsters.find { it.id == monsterId }
            ?.let { monster ->
                monster.count--
                if (monster.count == 0) {
                    removeMonster(monsterId)
                }
            }
    }

}

