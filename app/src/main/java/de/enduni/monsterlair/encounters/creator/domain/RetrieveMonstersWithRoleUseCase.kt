package de.enduni.monsterlair.encounters.creator.domain

import de.enduni.monsterlair.encounters.creator.view.EncounterCreatorFilter
import de.enduni.monsterlair.encounters.domain.model.MonsterWithRole
import de.enduni.monsterlair.encounters.persistence.MonsterWithRoleMapper
import de.enduni.monsterlair.monsters.persistence.MonsterRepository

class RetrieveMonstersWithRoleUseCase(
    private val repository: MonsterRepository,
    private val monsterWithRoleMapper: MonsterWithRoleMapper
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
        ).map { monsterWithRoleMapper.mapToMonsterWithRole(it, encounterLevel) }
    }


}

