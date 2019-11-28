package de.enduni.monsterlair.hazards.domain

import de.enduni.monsterlair.hazards.persistence.HazardRepository

class RetrieveHazardsUseCase(private val hazardRepository: HazardRepository) {

    suspend fun execute(): List<Hazard> {
        return hazardRepository.getHazards()
    }

}

