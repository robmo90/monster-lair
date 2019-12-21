package de.enduni.monsterlair.hazards.domain

import de.enduni.monsterlair.hazards.persistence.HazardRepository
import de.enduni.monsterlair.hazards.view.HazardFilter
import de.enduni.monsterlair.monsters.view.SortBy

class RetrieveHazardsUseCase(private val hazardRepository: HazardRepository) {

    suspend fun execute(hazardFilter: HazardFilter): List<Hazard> {
        return hazardRepository.getFilteredHazards(
            hazardFilter.string,
            hazardFilter.lowerLevel,
            hazardFilter.upperLevel,
            getSortByString(hazardFilter),
            hazardFilter.complexities
        )
    }

    private fun getSortByString(hazardFilter: HazardFilter): String {
        return when (hazardFilter.sortBy) {
            SortBy.TYPE -> "complexity"
            else -> hazardFilter.sortBy.value
        }
    }

    suspend fun getHazard(id: Long): Hazard {
        return hazardRepository.getHazard(id)
    }

}

