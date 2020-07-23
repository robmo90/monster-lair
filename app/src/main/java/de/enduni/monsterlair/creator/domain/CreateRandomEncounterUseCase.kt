package de.enduni.monsterlair.creator.domain

import de.enduni.monsterlair.common.domain.RandomEncounterTemplate
import de.enduni.monsterlair.common.getXp
import de.enduni.monsterlair.encounters.domain.model.Encounter
import de.enduni.monsterlair.encounters.domain.model.HazardWithRole
import de.enduni.monsterlair.encounters.domain.model.MonsterWithRole
import kotlin.random.Random.Default.nextBoolean

class CreateRandomEncounterUseCase {

    fun createRandomEncounter(
        originalEncounter: Encounter,
        monsters: List<MonsterWithRole>,
        hazards: List<HazardWithRole>,
        template: RandomEncounterTemplate
    ): Pair<List<MonsterWithRole>, List<HazardWithRole>> {
        val encounter = RandomEncounter(originalEncounter.level, originalEncounter.targetBudget)
        try {
            when (template) {
                RandomEncounterTemplate.BOSS_AND_LACKEYS -> staffBossAndLackeysEncounter(
                    encounter,
                    monsters
                )
                RandomEncounterTemplate.BOSS_AND_LIEUTENANT -> staffBossAndLieutenantEncounter(
                    encounter,
                    monsters
                )
                RandomEncounterTemplate.ELITE_ENEMIES -> staffEliteEnemiesEncounter(
                    encounter,
                    monsters
                )
                RandomEncounterTemplate.LIEUTENANT_AND_LACKEYS -> staffLieutenantAndLackeysEncounter(
                    encounter,
                    monsters
                )
                RandomEncounterTemplate.MATED_PAIR -> staffMatedPairEncounter(encounter, monsters)
                RandomEncounterTemplate.TROOP -> staffTroopEncounter(encounter, monsters)
                RandomEncounterTemplate.MOOK_SQUAD -> staffMookSquadEncounter(encounter, monsters)
                RandomEncounterTemplate.RANDOM -> staffRandomEncounter(encounter, monsters, hazards)
            }
            return Pair(encounter.monsters, encounter.hazards)
        } catch (ex: Exception) {
            throw RandomEncounterException()
        }

    }

    private fun staffBossAndLackeysEncounter(
        encounter: RandomEncounter,
        monsters: List<MonsterWithRole>
    ) {
        val bossMonster = monsters.filter { it.level == encounter.level + 2 }.random()
        encounter.monsters.add(bossMonster)

        val eligibleMooks = monsters.filter { it.level == encounter.level - 4 }
        encounter.tryToFillWithSameFamilyAndType(eligibleMooks, bossMonster)
    }

    private fun staffBossAndLieutenantEncounter(
        encounter: RandomEncounter,
        monsters: List<MonsterWithRole>
    ) {
        val bossMonster = monsters.filter { it.level == encounter.level + 2 }.random()
        encounter.monsters.add(bossMonster)

        val eligibleLieutenants = monsters.filter { it.level == encounter.level }
        encounter.tryToFillWithSameFamilyAndType(eligibleLieutenants, bossMonster)
    }

    private fun staffEliteEnemiesEncounter(
        encounter: RandomEncounter,
        monsters: List<MonsterWithRole>
    ) {
        staffWithSameLevelCreatureAndFillUp(encounter, monsters)
    }

    private fun staffMatedPairEncounter(
        encounter: RandomEncounter,
        monsters: List<MonsterWithRole>
    ) {
        staffWithSameLevelCreatureAndFillUp(encounter, monsters, 0)
    }

    private fun staffTroopEncounter(encounter: RandomEncounter, monsters: List<MonsterWithRole>) {
        val headMonster = monsters.filter { it.level == encounter.level }.random()
        encounter.monsters.add(headMonster)

        val eligibleMooks = monsters.filter { it.level == encounter.level - 2 }
        encounter.tryToFillWithSameFamilyAndType(eligibleMooks, headMonster)
    }

    private fun staffMookSquadEncounter(
        encounter: RandomEncounter,
        monsters: List<MonsterWithRole>
    ) {
        staffWithSameLevelCreatureAndFillUp(encounter, monsters, -4)
    }

    private fun staffWithSameLevelCreatureAndFillUp(
        encounter: RandomEncounter,
        monsters: List<MonsterWithRole>,
        offset: Int = 0
    ) {
        val monster = monsters.filter { it.level == encounter.level + offset }.random()
        encounter.fillUp(listOf(monster)) { encounter.staffMonster(listOf(monster)) }

        encounter.tryToFillWithSameFamilyAndType(monsters, monster)
    }

    private fun staffLieutenantAndLackeysEncounter(
        encounter: RandomEncounter,
        monsters: List<MonsterWithRole>
    ) {
        val lieutenant = monsters.filter { it.level == encounter.level }.random()
        encounter.monsters.add(lieutenant)

        val eligibleMooks = monsters.filter { it.level == encounter.level - 4 }
        encounter.tryToFillWithSameFamilyAndType(eligibleMooks, lieutenant)
    }

    private fun RandomEncounter.tryToFillWithSameFamilyAndType(
        monsters: List<MonsterWithRole>,
        reference: MonsterWithRole
    ) {
        val monster = monsters
            .filter { it.family == reference.family || it.type == reference.type }
            .random()
        fillUp(listOf(monster)) { fillUp { staffMonster(listOf(monster)) } }
    }

    private fun staffRandomEncounter(
        encounter: RandomEncounter,
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

    private fun RandomEncounter.fillUp(
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

    private fun RandomEncounter.staffHazard(hazards: List<HazardWithRole>) {
        if (hazards.isEmpty()) return
        val randomHazard = hazards.random()
        if (randomHazard.role.getXp(randomHazard.complexity) <= getAvailableBudget()) {
            this.hazards.add(randomHazard)
        }
    }

    private fun RandomEncounter.staffMonster(monsters: List<MonsterWithRole>) {
        if (monsters.isEmpty()) return
        val randomMonster = monsters.random()
        if (randomMonster.role.xp <= getAvailableBudget()) {
            this.monsters.add(randomMonster)
        }
    }

    private fun RandomEncounter.getAvailableBudget(): Int {
        return targetBudget - currentBudget
    }

    private fun RandomEncounter.canStillBeStaffed(
        monsters: List<MonsterWithRole> = emptyList(),
        hazards: List<HazardWithRole> = emptyList()
    ): Boolean {
        val budget = getAvailableBudget()
        val monsterAvailable = monsters.any { it.role.xp <= budget }
        val hazardsAvailable = hazards.any { it.role.getXp(it.complexity) <= budget }
        return monsterAvailable || hazardsAvailable
    }
}

data class RandomEncounter(
    val level: Int,
    val targetBudget: Int,
    val monsters: MutableList<MonsterWithRole> = mutableListOf(),
    val hazards: MutableList<HazardWithRole> = mutableListOf()
) {

    val currentBudget: Int
        get() {
            val monsterXp = this.monsters.map { it.role.xp }.sum()
            val hazardXp = this.hazards.map { it.role.getXp(it.complexity) }.sum()
            return monsterXp + hazardXp
        }

}

class RandomEncounterException : Throwable()