package de.enduni.monsterlair.hazards.view

data class HazardOverviewViewState(
    val hazards: List<HazardDisplayModel> = emptyList(),
    val hazardFilter: HazardFilter? = null
)

data class HazardFilter(
    val string: String? = null,
    val lowerLevel: Int = DEFAULT_LEVEL_LOWER,
    val upperLevel: Int = DEFAULT_LEVEL_UPPER,
    val type: HazardType = HazardType.ALL
) {

    companion object {

        const val DEFAULT_LEVEL_LOWER = -1
        const val DEFAULT_LEVEL_UPPER = 25

    }
}

enum class HazardType {
    ALL,
    SIMPLE,
    COMPLEX
}