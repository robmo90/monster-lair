package de.enduni.monsterlair.encounters.domain.model

class Encounter(
    val id: Long? = null,
    var name: String = "Encounter",
    val monsters: MutableList<EncounterMonster> = mutableListOf(),
    val hazards: MutableList<EncounterHazard> = mutableListOf(),
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

    fun addHazard(hazard: HazardWithRole) {
        val hazardAlreadyInList = hazards.any { it.id == hazard.id }
        if (hazardAlreadyInList) {
            incrementCount(hazardId = hazard.id)
        } else {
            hazards.add(
                EncounterHazard(
                    id = hazard.id,
                    hazard = hazard,
                    count = 1
                )
            )
        }
    }

    private fun removeMonster(monsterId: Long) {
        monsters.removeIf { it.id == monsterId }
    }

    private fun removeHazard(hazardId: Long) {
        hazards.removeIf { it.id == hazardId }
    }

    fun incrementCount(monsterId: Long? = null, hazardId: Long? = null) {
        monsterId?.let { id ->
            monsters.find { it.id == id }
                ?.let { it.count++ }
        }
        hazardId?.let { id ->
            hazards.find { it.id == id }
                ?.let { it.count++ }
        }
    }

    fun decrementCount(monsterId: Long? = null, hazardId: Long? = null) {
        monsterId?.let { id ->
            monsters.find { it.id == id }
                ?.let { monster ->
                    monster.count--
                    if (monster.count == 0) {
                        removeMonster(id)
                    }
                }
        }
        hazardId?.let { id ->
            hazards.find { it.id == id }
                ?.let { hazard ->
                    hazard.count--
                    if (hazard.count == 0) {
                        removeHazard(id)
                    }
                }
        }
    }

}

