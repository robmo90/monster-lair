package de.enduni.monsterlair.encounters.creator.view

import de.enduni.monsterlair.encounters.domain.model.CreatureRole
import de.enduni.monsterlair.encounters.domain.model.EncounterDifficulty
import de.enduni.monsterlair.monsters.domain.MonsterType

sealed class EncounterCreatorDisplayModel {

    data class Monster(
        val id: Long,
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
        val id: Long,
        val name: String,
        val type: MonsterType,
        val count: Int,
        val level: Int,
        val family: String,
        val role: CreatureRole
    ) : EncounterCreatorDisplayModel()


}