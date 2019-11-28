package de.enduni.monsterlair.hazards.domain

import de.enduni.monsterlair.hazards.persistence.HazardRepository
import de.enduni.monsterlair.hazards.view.HazardFilter

class RetrieveHazardsUseCase(private val hazardRepository: HazardRepository) {

    suspend fun execute(hazardFilter: HazardFilter): List<Hazard> {
        return hazardRepository.getFilteredHazards(
            hazardFilter.string,
            hazardFilter.lowerLevel,
            hazardFilter.upperLevel,
            hazardFilter.type
        )
    }

    suspend fun getHazard(id: Long): Hazard {
        return hazardRepository.getHazard(id)
    }

}

