package de.enduni.monsterlair.hazards.domain

import de.enduni.monsterlair.common.domain.*

data class HazardFilter(
    val searchTerm: String = "",
    val lowerLevel: Level = DEFAULT_LEVEL_LOWER,
    val upperLevel: Level = DEFAULT_LEVEL_UPPER,
    val complexities: List<Complexity> = emptyList(),
    val rarities: List<Rarity> = emptyList(),
    val traits: List<Trait> = emptyList(),
    val sortBy: SortBy = SortBy.NAME
) {

    companion object {

        const val DEFAULT_LEVEL_LOWER = -1
        const val DEFAULT_LEVEL_UPPER = 23

    }
}