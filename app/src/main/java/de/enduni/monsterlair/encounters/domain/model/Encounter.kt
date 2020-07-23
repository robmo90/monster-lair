package de.enduni.monsterlair.encounters.domain.model

import de.enduni.monsterlair.common.domain.Level
import de.enduni.monsterlair.common.getXp
import kotlin.math.abs
import kotlin.math.min

data class Encounter(
    val id: Long? = null,
    val name: String = "Encounter",
    val monsters: List<EncounterMonster> = emptyList(),
    val hazards: List<EncounterHazard> = emptyList(),
    val level: Level = 0,
    val numberOfPlayers: Int = 0,
    val targetDifficulty: EncounterDifficulty = EncounterDifficulty.TRIVIAL,
    val withoutProficiency: Boolean = false,
    val notes: String = ""
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

}

