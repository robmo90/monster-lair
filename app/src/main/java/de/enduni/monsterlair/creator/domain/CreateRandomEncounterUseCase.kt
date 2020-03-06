package de.enduni.monsterlair.creator.domain

import de.enduni.monsterlair.common.domain.RandomEncounter
import de.enduni.monsterlair.common.getXp
import de.enduni.monsterlair.encounters.domain.model.Encounter
import de.enduni.monsterlair.encounters.domain.model.HazardWithRole
import de.enduni.monsterlair.encounters.domain.model.MonsterWithRole
import kotlin.random.Random.Default.nextBoolean

class CreateRandomEncounterUseCase {

    fun createRandomEncounter(
        encounter: Encounter,
        monsters: List<MonsterWithRole>,
        hazards: List<HazardWithRole>,
        randomEncounter: RandomEncounter
    ) {
        encounter.clear()
        when (randomEncounter) {
            RandomEncounter.BOSS_AND_LACKEYS -> staffBossAndLackeysEncounter(encounter, monsters)
            RandomEncounter.BOSS_AND_LIEUTENANT -> staffBossAndLieutenantEncounter(
                encounter,
                monsters
            )
            RandomEncounter.ELITE_ENEMIES -> staffEliteEnemiesEncounter(encounter, monsters)
            RandomEncounter.LIEUTENANT_AND_LACKEYS -> staffLieutenantAndLackeysEncounter(
                encounter,
                monsters
            )
            RandomEncounter.MATED_PAIR -> staffMatedPairEncounter(encounter, monsters)
            RandomEncounter.TROOP -> staffTroopEncounter(encounter, monsters)
            RandomEncounter.MOOK_SQUAD -> staffMookSquadEncounter(encounter, monsters)
            RandomEncounter.RANDOM -> staffRandomEncounter(encounter, monsters, hazards)
        }
    }

    private fun staffBossAndLackeysEncounter(
        encounter: Encounter,
        monsters: List<MonsterWithRole>
    ) {
        val bossMonster = monsters.filter { it.level == encounter.level + 2 }.random()
        encounter.addMonster(bossMonster)

        val eligibleMooks = monsters.filter { it.level == encounter.level - 4 }
        encounter.tryToFillWithSameFamilyAndType(eligibleMooks, bossMonster)
    }

    private fun staffBossAndLieutenantEncounter(
        encounter: Encounter,
        monsters: List<MonsterWithRole>
    ) {
        val bossMonster = monsters.filter { it.level == encounter.level + 2 }.random()
        encounter.addMonster(bossMonster)

        val eligibleLieutenants = monsters.filter { it.level == encounter.level }
        encounter.tryToFillWithSameFamilyAndType(eligibleLieutenants, bossMonster)
    }

    private fun staffEliteEnemiesEncounter(encounter: Encounter, monsters: List<MonsterWithRole>) {
        staffWithSameLevelCreatureAndFillUp(encounter, monsters)
    }

    private fun staffMatedPairEncounter(encounter: Encounter, monsters: List<MonsterWithRole>) {
        staffWithSameLevelCreatureAndFillUp(encounter, monsters, 0)
    }

    private fun staffTroopEncounter(encounter: Encounter, monsters: List<MonsterWithRole>) {
        val headMonster = monsters.filter { it.level == encounter.level }.random()
        encounter.addMonster(headMonster)

        val eligibleMooks = monsters.filter { it.level == encounter.level - 2 }
        encounter.tryToFillWithSameFamilyAndType(eligibleMooks, headMonster)
    }

    private fun staffMookSquadEncounter(encounter: Encounter, monsters: List<MonsterWithRole>) {
        staffWithSameLevelCreatureAndFillUp(encounter, monsters, -4)
    }

    private fun staffWithSameLevelCreatureAndFillUp(
        encounter: Encounter,
        monsters: List<MonsterWithRole>,
        offset: Int = 0
    ) {
        val monster = monsters.filter { it.level == encounter.level + offset }.random()
        encounter.fillUp(listOf(monster)) { encounter.staffMonster(listOf(monster)) }

        encounter.tryToFillWithSameFamilyAndType(monsters, monster)
    }

    private fun staffLieutenantAndLackeysEncounter(
        encounter: Encounter,
        monsters: List<MonsterWithRole>
    ) {
        val lieutenant = monsters.filter { it.level == encounter.level }.random()
        encounter.addMonster(lieutenant)

        val eligibleMooks = monsters.filter { it.level <= encounter.level - 4 }
        encounter.tryToFillWithSameFamilyAndType(eligibleMooks, lieutenant)
    }

    private fun Encounter.tryToFillWithSameFamilyAndType(
        monsters: List<MonsterWithRole>,
        reference: MonsterWithRole
    ) {
        val monster = monsters
            .filter { it.family == reference.family || it.type == reference.type }
            .random()
        fillUp(listOf(monster)) { fillUp { staffMonster(listOf(monster)) } }
    }

    private fun staffRandomEncounter(
        encounter: Encounter,
        monsters: List<MonsterWithRole>,
        hazards: List<HazardWithRole>
    ) {
        encounter.fillUp(monsters, hazards) {
            if (nextBoolean()) {
                encounter.staffMonster(monsters)
            } else {
                encounter.staffHazard(hazards)
            }
        }
    }

    private fun Encounter.fillUp(
        monsters: List<MonsterWithRole> = emptyList(),
        hazards: List<HazardWithRole> = emptyList(),
        addOperation: () -> Unit
    ) {
        do {
            addOperation.invoke()
        } while (getAvailableBudget() >= 0 && canStillBeStaffed(
                monsters,
                hazards
            )
        )
    }

    private fun Encounter.staffHazard(hazards: List<HazardWithRole>) {
        if (hazards.isEmpty()) return
        val randomHazard = hazards.random()
        if (randomHazard.role.getXp(randomHazard.complexity) <= getAvailableBudget()) {
            addHazard(randomHazard)
        }
    }

    private fun Encounter.staffMonster(monsters: List<MonsterWithRole>) {
        if (monsters.isEmpty()) return
        val randomMonster = monsters.random()
        if (randomMonster.role.xp <= getAvailableBudget()) {
            addMonster(randomMonster)
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
        monsters: List<MonsterWithRole> = emptyList(),
        hazards: List<HazardWithRole> = emptyList()
    ): Boolean {
        val budget = getAvailableBudget()
        val monsterAvailable = monsters.any { it.role.xp <= budget }
        val hazardsAvailable = hazards.any { it.role.getXp(it.complexity) <= budget }
        return monsterAvailable || hazardsAvailable
    }
}