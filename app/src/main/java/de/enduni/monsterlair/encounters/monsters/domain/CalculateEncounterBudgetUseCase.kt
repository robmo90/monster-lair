package de.enduni.monsterlair.encounters.monsters.domain

import de.enduni.monsterlair.common.EncounterDifficulty
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import kotlin.math.abs
import kotlin.math.min

class CalculateEncounterBudgetUseCase {

    suspend fun execute(encounter: Encounter): EncounterBudget =
        withContext(Dispatchers.Default) {
            val currentBudget = encounter.monsters.map { it.monster.role.xp * it.count }.sum()
            val characterAdjustment = encounter.numberOfPlayers - 4
            val targetBudget = encounter.targetDifficulty.calculateBudget(characterAdjustment)

            var nearestDifficulty = EncounterDifficulty.TRIVIAL
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


            return@withContext EncounterBudget(
                currentBudget = currentBudget,
                targetBudget = targetBudget,
                currentDifficulty = nearestDifficulty
            )
        }

    private fun EncounterDifficulty.calculateBudget(characterAdjustment: Int): Int {
        return this.budget + this.characterAdjustment * characterAdjustment
    }

}

data class EncounterBudget(
    val currentBudget: Int,
    val targetBudget: Int,
    val currentDifficulty: EncounterDifficulty
)

enum class CreatureRole(val xp: Int) {
    TOO_LOW(0),
    LOW_LACKEY(10),
    MODERATE_LACKEY(15),
    STANDARD_LACKEY(20),
    STANDARD_CREATURE(30),
    LOW_BOSS(40),
    MODERATE_BOSS(60),
    SEVERE_BOSS(80),
    EXTREME_BOSS(120),
    SOLO_BOSS(160),
    TOO_HIGH(240)
}