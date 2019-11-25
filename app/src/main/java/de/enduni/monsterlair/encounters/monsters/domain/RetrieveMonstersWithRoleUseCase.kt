package de.enduni.monsterlair.encounters.monsters.domain

import de.enduni.monsterlair.common.persistence.MonsterRepository
import de.enduni.monsterlair.encounters.monsters.view.EncounterCreatorFilter
import de.enduni.monsterlair.monsters.domain.Monster
import de.enduni.monsterlair.monsters.domain.MonsterType

class RetrieveMonstersWithRoleUseCase(
    private val repository: MonsterRepository
) {

    suspend fun execute(
        filter: EncounterCreatorFilter,
        encounterLevel: Int
    ): List<MonsterWithRole> {
        val filterString = if (filter.string.isNullOrEmpty()) "\"%\"" else "\"%${filter.string}%\""
        return repository.getMonsters(
            filterString,
            filter.lowerLevel,
            filter.upperLevel,
            filter.sortBy.value
        ).mapToMonstersWithRole(encounterLevel)
    }

    private fun List<Monster>.mapToMonstersWithRole(encounterLevel: Int): List<MonsterWithRole> {
        return this.map {
            MonsterWithRole(
                id = it.id,
                name = it.name,
                url = it.url,
                family = it.family,
                level = it.level,
                alignment = it.alignment,
                type = it.type,
                size = it.size,
                role = it.determineRole(encounterLevel)
            )
        }
    }

    private fun Monster.determineRole(encounterLevel: Int): CreatureRole {
        val normalizedLevel = this.level - encounterLevel
        return if (normalizedLevel <= -5) {
            CreatureRole.TOO_LOW
        } else {
            when (normalizedLevel) {
                -4 -> CreatureRole.LOW_LACKEY
                -3 -> CreatureRole.MODERATE_LACKEY
                -2 -> CreatureRole.STANDARD_LACKEY
                -1 -> CreatureRole.STANDARD_CREATURE
                0 -> CreatureRole.LOW_BOSS
                1 -> CreatureRole.MODERATE_BOSS
                2 -> CreatureRole.SEVERE_BOSS
                3 -> CreatureRole.EXTREME_BOSS
                4 -> CreatureRole.SOLO_BOSS
                else -> CreatureRole.TOO_HIGH
            }
        }
    }

}

data class MonsterWithRole(
    val id: Int,
    val name: String,
    val url: String,
    val family: String,
    val level: Int,
    val alignment: String,
    val type: MonsterType,
    val size: String,
    val role: CreatureRole
)
