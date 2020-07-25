package de.enduni.monsterlair.treasure.domain

data class TreasureRecommendation(
    val itemValue: Int,
    val adjustmentNeeded: Boolean,
    val permanentItems: List<PermanentItem>,
    val permanentItemAdjustment: Int,
    val consumableItems: List<ConsumableItem>,
    val consumableItemAdjustment: Int,
    val currency: Int
)