package de.enduni.monsterlair.treasure.domain

import de.enduni.monsterlair.common.domain.*
import de.enduni.monsterlair.monsters.view.SortBy

data class TreasureFilter(
    val searchTerm: String = "",
    val lowerLevel: Level = 0,
    val upperLevel: Level = 25,
    val lowerGoldCost: Cost = null,
    val upperGoldCost: Cost = null,
    val categories: List<TreasureCategory> = emptyList(),
    val rarities: List<Rarity> = emptyList(),
    val traits: List<Trait> = emptyList(),
    val sortBy: SortBy = SortBy.NAME
)