package de.enduni.monsterlair.treasure.domain

import de.enduni.monsterlair.common.domain.Rarity
import de.enduni.monsterlair.common.domain.TreasureCategory
import de.enduni.monsterlair.monsters.view.SortBy

data class TreasureFilter(
    val searchTerm: String = "",
    val lowerLevel: Int = 0,
    val upperLevel: Int = 25,
    val lowerGoldCost: Double? = null,
    val upperGoldCost: Double? = null,
    val categories: List<TreasureCategory> = emptyList(),
    val rarities: List<Rarity> = emptyList(),
    val traits: List<String> = emptyList(),
    val sortBy: SortBy = SortBy.NAME
)