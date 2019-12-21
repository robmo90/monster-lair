package de.enduni.monsterlair.encounters.creator.view

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import de.enduni.monsterlair.encounters.domain.model.EncounterDifficulty

sealed class EncounterCreatorDisplayModel {

    data class Danger(
        val type: DangerType,
        val id: Long,
        val name: String,
        @DrawableRes val icon: Int,
        val level: Int,
        val label: String,
        @StringRes val labelRes: Int? = null,
        val xp: Int,
        val url: String,
        val originalType: String
    ) : EncounterCreatorDisplayModel()

    data class EncounterDetail(
        val name: String,
        val level: Int,
        val numberOfPlayers: Int,
        val targetDifficulty: EncounterDifficulty
    ) : EncounterCreatorDisplayModel()

    data class EncounterBudget(
        val currentBudget: Int,
        val targetBudget: Int,
        val currentDifficulty: EncounterDifficulty
    ) : EncounterCreatorDisplayModel()

    data class DangerForEncounter(
        val type: DangerType,
        val id: Long,
        val name: String,
        @DrawableRes val icon: Int,
        val count: Int,
        val level: Int,
        val label: String,
        @StringRes val labelRes: Int? = null,
        @StringRes val role: Int,
        val xp: Int,
        val url: String
    ) : EncounterCreatorDisplayModel()

}

enum class DangerType {
    MONSTER,
    HAZARD
}
