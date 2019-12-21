package de.enduni.monsterlair.hazards.view

import de.enduni.monsterlair.common.domain.Complexity
import de.enduni.monsterlair.monsters.view.SortBy

data class HazardOverviewViewState(
    val hazards: List<HazardDisplayModel> = emptyList(),
    val hazardFilter: HazardFilter? = null
)

data class HazardFilter(
    val string: String? = null,
    val lowerLevel: Int = DEFAULT_LEVEL_LOWER,
    val upperLevel: Int = DEFAULT_LEVEL_UPPER,
    val complexities: List<Complexity> = listOf(),
    val sortBy: SortBy = SortBy.NAME
) {

    companion object {

        const val DEFAULT_LEVEL_LOWER = -1
        const val DEFAULT_LEVEL_UPPER = 25

    }
}