package de.enduni.monsterlair.creator.view

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
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
        val xp: Int,
        val url: String?,
        val originalType: String,
        val customMonster: Boolean
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
        @DrawableRes val icon: Int,
        val count: Int,
        val level: Int,
        val label: String,
        @StringRes val labelRes: Int? = null,
        @StringRes val role: Int,
        val xp: Int,
        val url: String?,
        val customMonster: Boolean
    ) : EncounterCreatorDisplayModel()

}

enum class DangerType {
    MONSTER,
    HAZARD
}
