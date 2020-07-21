package de.enduni.monsterlair.treasure.domain

import de.enduni.monsterlair.common.domain.TreasureCategory
import de.enduni.monsterlair.monsters.view.SortBy

data class TreasureFilter(
    val searchString: String = "",
    val lowerLevel: Int = 0,
    val upperLevel: Int = 25,
    val categories: List<TreasureCategory> = emptyList(),
    val traits: List<String> = emptyList(),
    val sortBy: SortBy = SortBy.NAME
)