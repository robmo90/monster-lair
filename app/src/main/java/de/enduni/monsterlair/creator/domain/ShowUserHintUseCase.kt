package de.enduni.monsterlair.creator.domain

import android.content.Context
import androidx.core.content.edit

class ShowUserHintUseCase(context: Context) {

    private val sharedPreferences = context.getSharedPreferences(
        "MonsterLair",
        Context.MODE_PRIVATE
    )

    suspend fun showUserHint(): Boolean {
        return sharedPreferences.getBoolean(USER_HINT, true)
    }

    suspend fun markAsShown() {
        return sharedPreferences.edit { putBoolean(USER_HINT, false) }
    }

    companion object {

        private const val USER_HINT = "showEncounterCreatorUserHint"
    }

}