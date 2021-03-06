package de.enduni.monsterlair.treasure.domain

import de.enduni.monsterlair.common.domain.*

data class TreasureFilter(
    val searchTerm: String = "",
    val lowerLevel: Level = 0,
    val upperLevel: Level = 28,
    val lowerGoldCost: Cost = null,
    val upperGoldCost: Cost = null,
    val categories: List<TreasureCategory> = emptyList(),
    val rarities: List<Rarity> = emptyList(),
    val traits: List<Trait> = emptyList(),
    val sortBy: SortBy = SortBy.NAME
)