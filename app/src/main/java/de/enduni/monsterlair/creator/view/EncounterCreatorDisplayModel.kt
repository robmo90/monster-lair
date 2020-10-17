package de.enduni.monsterlair.creator.view

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import de.enduni.monsterlair.common.domain.*
import de.enduni.monsterlair.encounters.domain.model.EncounterDifficulty

sealed class EncounterCreatorDisplayModel {

    data class Danger(
        val type: DangerType,
        val id: String,
        val name: String,
        @DrawableRes val icon: Int,
        val level: Int,
        val label: String,
        @StringRes val labelRes: Int? = null,
        @StringRes val roleDescription: Int,
        val xp: Int,
        val url: String,
        val originalType: String,
        val source: Source,
        val alignment: Alignment?,
        val rarity: Rarity,
        val size: Size?,
        val traits: List<String>,
        val description: String
    ) : EncounterCreatorDisplayModel()

    data class EncounterDetails(
        val currentBudget: Int,
        val targetBudget: Int,
        val level: Int,
        val numberOfPlayers: Int,
        val targetDifficulty: EncounterDifficulty
    ) : EncounterCreatorDisplayModel()

    data class DangerForEncounter(
        val type: DangerType,
        val id: String,
        val name: String,
        val strength: Strength,
        @DrawableRes val icon: Int,
        val count: Int,
        val level: Int,
        val label: String,
        @StringRes val labelRes: Int? = null,
        @StringRes val role: Int,
        val xp: Int,
        val url: String,
        val source: Source,
        val alignment: Alignment?,
        val rarity: Rarity,
        val size: Size?,
        val traits: List<String>,
        val description: String
    ) : EncounterCreatorDisplayModel()

}

enum class DangerType {
    MONSTER,
    HAZARD
}
