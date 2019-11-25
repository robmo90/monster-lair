package de.enduni.monsterlair.encounters.monsters.view

import de.enduni.monsterlair.common.EncounterDifficulty
import de.enduni.monsterlair.encounters.monsters.domain.CreatureRole
import de.enduni.monsterlair.monsters.domain.MonsterType

sealed class EncounterCreatorDisplayModel {

    data class Monster(
        val id: Int,
        val name: String,
        val type: MonsterType,
        val level: Int,
        val family: String,
        val role: CreatureRole
    ) : EncounterCreatorDisplayModel()

    data class EncounterInformation(
        val currentBudget: Int,
        val targetBudget: Int,
        val currentDifficulty: EncounterDifficulty,
        val targetDifficulty: EncounterDifficulty
    ) : EncounterCreatorDisplayModel()

    data class MonsterForEncounter(
        val id: Int,
        val name: String,
        val type: MonsterType,
        val count: Int,
        val level: Int,
        val family: String,
        val role: CreatureRole
    ) : EncounterCreatorDisplayModel()


}