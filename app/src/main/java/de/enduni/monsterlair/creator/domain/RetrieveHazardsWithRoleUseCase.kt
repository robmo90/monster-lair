package de.enduni.monsterlair.creator.domain

import de.enduni.monsterlair.common.getXp
import de.enduni.monsterlair.creator.view.DangerType
import de.enduni.monsterlair.encounters.domain.model.Encounter
import de.enduni.monsterlair.encounters.domain.model.HazardWithRole
import de.enduni.monsterlair.encounters.persistence.HazardWithRoleMapper
import de.enduni.monsterlair.hazards.persistence.HazardRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class RetrieveHazardsWithRoleUseCase(
    private val repository: HazardRepository,
    private val hazardWithRoleMapper: HazardWithRoleMapper
) {

    suspend fun getFilteredHazards(
        filter: EncounterCreatorFilter,
        encounter: Encounter
    ) = withContext(Dispatchers.Default) {
        if (filter.dangerTypes.isNotEmpty() && !filter.dangerTypes.contains(DangerType.HAZARD)) {
            return@withContext emptyList<HazardWithRole>()
        }
        repository.getFilteredHazards(
            filter.searchTerm,
            filter.complexities,
            filter.rarities,
            filter.lowerLevel,
            filter.upperLevel,
            filter.sortBy.getStringForHazard(),
            filter.traits
        )
            .map { hazardWithRoleMapper.mapToHazardWithRole(it, encounter.level) }
            .filterHazardWithinBudget(filter = filter.withinBudget, encounter = encounter)
    }

    suspend fun getHazard(
        hazardId: String,
        encounter: Encounter
    ) = withContext(Dispatchers.Default) {
        repository.getHazard(hazardId)
            .let { hazardWithRoleMapper.mapToHazardWithRole(it, encounter.level) }
    }

    private fun List<HazardWithRole>.filterHazardWithinBudget(
        filter: Boolean,
        encounter: Encounter
    ): List<HazardWithRole> {
        return if (filter) {
            val maxXp = encounter.targetBudget - encounter.currentBudget
            this.filter { it.role.getXp(it.complexity) <= maxXp }
        } else {
            this
        }
    }
}

