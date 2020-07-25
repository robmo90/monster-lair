package de.enduni.monsterlair.creator.domain

import android.content.Context
import com.samskivert.mustache.Mustache
import de.enduni.monsterlair.R
import de.enduni.monsterlair.treasure.domain.CreateTreasureRecommendationUseCase

class CreateTreasureRecommendationTextUseCase(
    private val context: Context,
    private val createTreasureRecommendationUseCase: CreateTreasureRecommendationUseCase
) {

    fun execute(level: Int, numberOfPlayers: Int): String {


        val recommendation = createTreasureRecommendationUseCase.execute(level, numberOfPlayers)

        val templateBytes =
            context.resources.openRawResource(R.raw.treasure_recommendation_template).readBytes()
        val template = Mustache.compiler().compile(String(templateBytes))

        return template.execute(recommendation)
    }

}