package de.enduni.monsterlair.encounters.domain.model

import de.enduni.monsterlair.common.getXp
import kotlin.math.abs
import kotlin.math.min

data class Encounter(
    val id: Long? = null,
    var name: String = "Encounter",
    val monsters: MutableList<EncounterMonster> = mutableListOf(),
    val hazards: MutableList<EncounterHazard> = mutableListOf(),
    var level: Int,
    var numberOfPlayers: Int,
    var targetDifficulty: EncounterDifficulty
) {

    val targetBudget: Int
        get() {
            val characterAdjustment = this.numberOfPlayers - 4
            return targetDifficulty.calculateBudget(characterAdjustment)
        }
    val currentDifficulty: EncounterDifficulty
        get() {
            val characterAdjustment = this.numberOfPlayers - 4
            var nearestDifficulty = EncounterDifficulty.TRIVIAL
            val currentBudget = this.currentBudget
            var minimalBudgetDifference = Integer.MAX_VALUE
            for (difficulty in EncounterDifficulty.values()) {
                val calculatedBudget = difficulty.calculateBudget(characterAdjustment)
                val difference =
                    abs(min(currentBudget - calculatedBudget, calculatedBudget - currentBudget))
                if (difference < minimalBudgetDifference) {
                    minimalBudgetDifference = difference
                    nearestDifficulty = difficulty
                }
            }
            return nearestDifficulty
        }
    val currentBudget: Int
        get() {
            val monsterXp = this.monsters
                .map { it.monster.role.xp * it.count }.sum()
            val hazardXp =
                this.hazards.map { it.hazard.role.getXp(it.hazard.complexity) * it.count }.sum()
            return monsterXp + hazardXp
        }

    private fun EncounterDifficulty.calculateBudget(characterAdjustment: Int): Int {
        return this.budget + this.characterAdjustment * characterAdjustment
    }


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

