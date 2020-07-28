package de.enduni.monsterlair.treasure.domain

import de.enduni.monsterlair.common.domain.Level
import de.enduni.monsterlair.common.domain.TreasureCategory
import de.enduni.monsterlair.treasure.repository.TreasureRepository

class CreateRandomTreasureUseCase(
    private val treasureRecommendationUseCase: CreateTreasureRecommendationUseCase,
    private val treasureRepository: TreasureRepository
) {

    suspend fun execute(
        level: Level,
        numberOfPlayers: Int
    ): Triple<TreasureRecommendation, List<Treasure>, List<Treasure>> {
        val recommendation = treasureRecommendationUseCase.execute(level, numberOfPlayers)

        val permanentItemLevels = recommendation.permanentItems.map { it.level }
        val adjustedPermanentItems = recommendation.permanentItems + IntRange(
            0,
            recommendation.permanentItemAdjustment - 1
        ).map {
            PermanentItem(permanentItemLevels.random(), 1)
        }
        val permanentItems = adjustedPermanentItems.flatMap { item ->
            IntRange(0, item.count - 1).map {
                treasureRepository.getTreasures(
                    TreasureFilter(
                        upperLevel = item.level,
                        lowerLevel = item.level,
                        lowerGoldCost = 1.0,
                        categories = TreasureCategory.values().filter { it.permanentItem }
                    )
                ).random()
            }
        }

        val consumableItemLevels = recommendation.consumableItems.map { it.level }
        val adjustedConsumableItems = recommendation.consumableItems + IntRange(
            0,
            recommendation.consumableItemAdjustment - 1
        ).map {
            ConsumableItem(consumableItemLevels.random(), 1)
        }
        val consumableItems = adjustedConsumableItems.flatMap { item ->
            IntRange(0, item.count - 1).map {
                treasureRepository.getTreasures(
                    TreasureFilter(
                        lowerGoldCost = 1.0,
                        upperLevel = item.level,
                        lowerLevel = item.level,
                        categories = TreasureCategory.values().filter { !it.permanentItem }
                    )
                ).random()
            }
        }

        return Triple(recommendation, permanentItems, consumableItems)
    }

}

