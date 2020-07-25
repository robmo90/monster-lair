package de.enduni.monsterlair.treasure.domain

import android.content.Context
import com.samskivert.mustache.Mustache
import de.enduni.monsterlair.R
import de.enduni.monsterlair.common.domain.Level
import de.enduni.monsterlair.common.getStringRes

class CreateRandomTreasureTextUseCase(
    private val context: Context,
    private val createTreasureRecommendationUseCase: CreateRandomTreasureUseCase
) {

    suspend fun execute(level: Int, numberOfPlayers: Int): String {
        val generatedTreasure = createTreasureRecommendationUseCase.execute(level, numberOfPlayers)

        val templateBytes =
            context.resources.openRawResource(R.raw.generated_treasure_template).readBytes()
        val template = Mustache.compiler().compile(String(templateBytes))

        return template.execute(
            GeneratedTreasure(
                level = level,
                numberOfPlayers = numberOfPlayers,
                itemValue = generatedTreasure.first.itemValue,
                permanentItems = generatedTreasure.second.map { it.mapForExport() }
                    .sortedByDescending { it.level },
                consumableItems = generatedTreasure.third.map { it.mapForExport() }
                    .sortedByDescending { it.level },
                currency = generatedTreasure.first.currency
            )
        )
    }

    private fun Treasure.mapForExport() = TreasureForExport(
        name = name,
        url = url,
        level = level,
        category = context.getString(category.getStringRes()),
        price = price,
        source = source,
        traits = traits.joinToString()
    )

}

data class GeneratedTreasure(
    val level: Level,
    val numberOfPlayers: Int,
    val itemValue: Int,
    val permanentItems: List<TreasureForExport>,
    val consumableItems: List<TreasureForExport>,
    val currency: Int
)

data class TreasureForExport(
    val name: String,
    val url: String,
    val level: Int,
    val category: String,
    val price: String,
    val source: String,
    val traits: String
)
