package de.enduni.monsterlair.encounters.creator.domain

import de.enduni.monsterlair.encounters.creator.view.EncounterCreatorFilter
import de.enduni.monsterlair.encounters.domain.model.HazardWithRole
import de.enduni.monsterlair.encounters.persistence.HazardWithRoleMapper
import de.enduni.monsterlair.hazards.persistence.HazardRepository
import de.enduni.monsterlair.hazards.view.HazardType

class RetrieveHazardsWithRoleUseCase(
    private val repository: HazardRepository,
    private val hazardWithRoleMapper: HazardWithRoleMapper
) {

    suspend fun execute(
        filter: EncounterCreatorFilter,
        encounterLevel: Int
    ): List<HazardWithRole> {
        return repository.getFilteredHazards(
            filter.string,
            filter.lowerLevel,
            filter.upperLevel,
            HazardType.ALL
        ).map { hazardWithRoleMapper.mapToHazardWithRole(it, encounterLevel) }
    }

    suspend fun findSingleHazard(
        id: Long,
        encounterLevel: Int
    ): HazardWithRole {
        return repository.getHazard(id)
            .let { hazardWithRoleMapper.mapToHazardWithRole(it, encounterLevel) }
    }

}

