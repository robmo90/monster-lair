package de.enduni.monsterlair.creator.domain

import de.enduni.monsterlair.creator.view.DangerType
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

    suspend fun getFilteredMonsters(
        filter: EncounterCreatorFilter,
        encounter: Encounter
    ) = withContext(Dispatchers.Default) {
        if (filter.dangerTypes.isNotEmpty() && !filter.dangerTypes.contains(DangerType.MONSTER)) {
            return@withContext emptyList<MonsterWithRole>()
        }
        repository.getMonsters(
            filter.searchTerm,
            filter.lowerLevel,
            filter.upperLevel,
            filter.sortBy.value,
            filter.types,
            filter.sizes,
            filter.alignments,
            filter.rarities,
            filter.traits
        )
            .map {
                monsterWithRoleMapper.mapToMonsterWithRole(
                    it,
                    encounter.level,
                    encounter.useProficiencyWithoutLevel
                )
            }
            .filterMonstersWithinBudget(filter = filter.withinBudget, encounter = encounter)
    }

    suspend fun getMonster(
        monsterId: String,
        encounter: Encounter
    ) = withContext(Dispatchers.Default) {
        repository.getMonster(monsterId)
            .let {
                monsterWithRoleMapper.mapToMonsterWithRole(
                    it,
                    encounter.level,
                    encounter.useProficiencyWithoutLevel
                )
            }
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

