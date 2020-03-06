package de.enduni.monsterlair.creator.domain

import de.enduni.monsterlair.common.getXp
import de.enduni.monsterlair.encounters.domain.model.Encounter
import de.enduni.monsterlair.encounters.domain.model.HazardWithRole
import de.enduni.monsterlair.encounters.domain.model.MonsterWithRole
import kotlin.random.Random.Default.nextBoolean

class CreateRandomEncounterUseCase {

    fun createRandomEncounter(
        encounter: Encounter,
        monsters: List<MonsterWithRole>,
        hazards: List<HazardWithRole>
    ) {
        encounter.clear()
        do {
            if (nextBoolean()) {
                addMonster(encounter, monsters)
            } else {
                addHazard(encounter, hazards)
            }
        } while (encounter.getAvailableBudget() >= 0 && encounter.canStillBeStaffed(
                monsters,
                hazards
            )
        )
    }

    private fun addHazard(encounter: Encounter, hazards: List<HazardWithRole>) {
        if (hazards.isEmpty()) return
        val randomHazard = hazards.random()
        if (randomHazard.role.getXp(randomHazard.complexity) <= encounter.getAvailableBudget()) {
            encounter.addHazard(randomHazard)
        }
    }

    private fun addMonster(encounter: Encounter, monsters: List<MonsterWithRole>) {
        if (monsters.isEmpty()) return
        val randomMonster = monsters.random()
        if (randomMonster.role.xp <= encounter.getAvailableBudget()) {
            encounter.addMonster(randomMonster)
        }
    }

    private fun Encounter.getAvailableBudget(): Int {
        return targetBudget - currentBudget
    }

    private fun Encounter.clear() {
        this.monsters.clear()
        this.hazards.clear()
    }


    private fun Encounter.canStillBeStaffed(
        monsters: List<MonsterWithRole>,
        hazards: List<HazardWithRole>
    ): Boolean {
        val budget = getAvailableBudget()
        val monsterAvailable = monsters.any { it.role.xp < budget }
        val hazardsAvailable = hazards.any { it.role.getXp(it.complexity) < budget }
        return monsterAvailable || hazardsAvailable
    }
}