package de.enduni.monsterlair.creator.domain

import android.content.Context
import com.samskivert.mustache.Mustache
import de.enduni.monsterlair.R

class CreateTreasureRecommendationUseCase(
    private val context: Context
) {

    fun execute(level: Int, numberOfPlayers: Int): String {
        val treasureValueForLevel = treasureValueByLevel.getValue(level)
        val treasureBudget = treasureValueForLevel.total
        val playerAdjustment = numberOfPlayers - 4
        val currency =
            treasureValueForLevel.currency + playerAdjustment * treasureValueForLevel.currencyAdjustment
        val permanentItems = estimatePermanentItems(level)
        val consumableItems = estimateConsumableItems(level)
        val permanentItemAdjustment = playerAdjustment
        val consumableItemAdjustment = playerAdjustment * 2

        val recommendation = TreasureRecommendation(
            itemValue = treasureBudget,
            adjustmentNeeded = playerAdjustment != 0,
            permanentItems = permanentItems,
            permanentItemAdjustment = permanentItemAdjustment,
            consumableItems = consumableItems,
            consumableItemAdjustment = consumableItemAdjustment,
            currency = currency
        )

        val templateBytes =
            context.resources.openRawResource(R.raw.treasure_recommendation_template).readBytes()
        val template = Mustache.compiler().compile(String(templateBytes))

        return template.execute(recommendation)
    }

    private fun estimatePermanentItems(level: Int): List<PermanentItem> {
        return if (level == 20) {
            listOf(PermanentItem(20, 4))
        } else {
            listOf(
                PermanentItem(level, 2),
                PermanentItem(level + 1, 2)
            )
        }

    }

    private fun estimateConsumableItems(level: Int): List<ConsumableItem> {
        return when (level) {
            1 -> listOf(
                ConsumableItem(2, 2), ConsumableItem(1, 3)
            )
            20 -> listOf(
                ConsumableItem(20, 4), ConsumableItem(19, 2)
            )
            else -> listOf(
                ConsumableItem(level + 1, 2), ConsumableItem(level, 2), ConsumableItem(level - 1, 2)
            )
        }
    }

    companion object {

        val treasureValueByLevel = mapOf(
            Pair(1, TreasureValue(175, 40, 10)),
            Pair(2, TreasureValue(300, 70, 18)),
            Pair(3, TreasureValue(500, 120, 30)),
            Pair(4, TreasureValue(850, 200, 50)),
            Pair(5, TreasureValue(1350, 320, 80)),
            Pair(6, TreasureValue(2000, 500, 125)),
            Pair(7, TreasureValue(2900, 720, 180)),
            Pair(8, TreasureValue(4000, 1000, 250)),
            Pair(9, TreasureValue(5700, 1400, 350)),
            Pair(10, TreasureValue(8000, 2000, 500)),
            Pair(11, TreasureValue(11500, 2800, 700)),
            Pair(12, TreasureValue(16500, 4000, 1000)),
            Pair(13, TreasureValue(25000, 6000, 1500)),
            Pair(14, TreasureValue(36500, 9000, 2250)),
            Pair(15, TreasureValue(54500, 13000, 3250)),
            Pair(16, TreasureValue(82500, 20000, 5000)),
            Pair(17, TreasureValue(128000, 30000, 7500)),
            Pair(18, TreasureValue(208000, 48000, 12000)),
            Pair(19, TreasureValue(350000, 80000, 20000)),
            Pair(20, TreasureValue(490000, 140000, 35000))
        )

    }


    data class TreasureValue(
        val total: Int,
        val currency: Int,
        val currencyAdjustment: Int
    )

    data class TreasureRecommendation(
        val itemValue: Int,
        val adjustmentNeeded: Boolean,
        val permanentItems: List<PermanentItem>,
        val permanentItemAdjustment: Int,
        val consumableItems: List<ConsumableItem>,
        val consumableItemAdjustment: Int,
        val currency: Int
    )

    data class PermanentItem(
        val level: Int,
        val count: Int
    )

    data class ConsumableItem(
        val level: Int,
        val count: Int
    )
}