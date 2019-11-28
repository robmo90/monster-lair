package de.enduni.monsterlair.encounters.creator.view

import de.enduni.monsterlair.encounters.domain.model.EncounterDifficulty
import de.enduni.monsterlair.encounters.domain.model.HazardRole
import de.enduni.monsterlair.encounters.domain.model.MonsterRole
import de.enduni.monsterlair.hazards.domain.Complexity
import de.enduni.monsterlair.monsters.domain.MonsterType

sealed class EncounterCreatorDisplayModel {

    data class Monster(
        val id: Long,
        val name: String,
        val type: MonsterType,
        val level: Int,
        val family: String,
        val role: MonsterRole
    ) : EncounterCreatorDisplayModel(), Sortable {
        override fun name() = this.name

        override fun level() = this.level
    }

    data class Hazard(
        val id: Long,
        val name: String,
        val complexity: Complexity,
        val level: Int,
        val role: HazardRole
    ) : EncounterCreatorDisplayModel(), Sortable {
        override fun name() = this.name

        override fun level() = this.level
    }

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
        val role: MonsterRole
    ) : EncounterCreatorDisplayModel(), Sortable {
        override fun name() = this.name

        override fun level() = this.level
    }

    data class HazardForEncounter(
        val id: Long,
        val name: String,
        val complexity: Complexity,
        val count: Int,
        val level: Int,
        val role: HazardRole
    ) : EncounterCreatorDisplayModel(), Sortable {
        override fun name() = this.name

        override fun level() = this.level
    }


}

interface Sortable {
    fun name(): String
    fun level(): Int
}