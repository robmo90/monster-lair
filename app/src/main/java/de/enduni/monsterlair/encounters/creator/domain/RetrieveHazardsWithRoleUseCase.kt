package de.enduni.monsterlair.encounters.creator.domain

import de.enduni.monsterlair.encounters.creator.view.EncounterCreatorFilter
import de.enduni.monsterlair.encounters.domain.model.HazardWithRole
import de.enduni.monsterlair.encounters.persistence.HazardWithRoleMapper
import de.enduni.monsterlair.hazards.persistence.HazardRepository

class RetrieveHazardsWithRoleUseCase(
    private val repository: HazardRepository,
    private val hazardWithRoleMapper: HazardWithRoleMapper
) {

    suspend fun execute(
        filter: EncounterCreatorFilter,
        encounterLevel: Int
    ): List<HazardWithRole> {
        return repository.getHazards()
            .map { hazardWithRoleMapper.mapToHazardWithRole(it, encounterLevel) }
    }


}

