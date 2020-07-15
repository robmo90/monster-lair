package de.enduni.monsterlair.treasure.domain

import de.enduni.monsterlair.common.domain.Rarity
import de.enduni.monsterlair.common.domain.Source
import de.enduni.monsterlair.common.domain.TreasureCategory

data class Treasure(
    val id: String,
    val name: String,
    val url: String,
    val level: Int,
    val category: TreasureCategory,
    val price: String,
    val priceInGp: Double,
    val source: String,
    val sourceType: Source,
    val rarity: Rarity,
    val traits: List<String>
)