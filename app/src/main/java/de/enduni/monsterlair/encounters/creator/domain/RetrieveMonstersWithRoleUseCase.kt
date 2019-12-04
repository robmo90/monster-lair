package de.enduni.monsterlair.encounters.creator.domain

import de.enduni.monsterlair.encounters.creator.view.EncounterCreatorFilter
import de.enduni.monsterlair.encounters.domain.model.Encounter
import de.enduni.monsterlair.encounters.domain.model.MonsterWithRole
import de.enduni.monsterlair.encounters.persistence.MonsterWithRoleMapper
import de.enduni.monsterlair.monsters.persistence.MonsterRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class RetrieveMonstersWithRoleUseCase(
    private val repository: MonsterRepository,
    private val monsterWithRoleMapper: MonsterWithRoleMapper
) {

    suspend fun execute(
        filter: EncounterCreatorFilter,
        encounter: Encounter
    ) = withContext(Dispatchers.Default) {
        repository.getMonsters(
            filter.string,
            filter.lowerLevel,
            filter.upperLevel,
            filter.sortBy.value
        )
            .map { monsterWithRoleMapper.mapToMonsterWithRole(it, encounter.level) }
            .filterMonstersWithinBudget(filter = filter.withinBudget, encounter = encounter)
    }


    private fun List<MonsterWithRole>.filterMonstersWithinBudget(
        filter: Boolean,
        encounter: Encounter
    ): List<MonsterWithRole> {
        return if (filter) {
            val maxXp = encounter.targetBudget - encounter.currentBudget
            this.filter { it.role.xp <= maxXp }
        } else {
            this
        }
    }
}

