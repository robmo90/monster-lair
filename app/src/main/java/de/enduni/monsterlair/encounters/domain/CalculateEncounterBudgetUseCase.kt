package de.enduni.monsterlair.encounters.domain

import de.enduni.monsterlair.encounters.domain.model.Encounter
import de.enduni.monsterlair.encounters.domain.model.EncounterBudget
import de.enduni.monsterlair.encounters.domain.model.EncounterDifficulty
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

