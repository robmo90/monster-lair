package de.enduni.monsterlair.common.view

import android.content.SharedPreferences
import androidx.core.content.edit
import timber.log.Timber

class DarkModeManager(
    private val sharedPreferences: SharedPreferences
) {

    companion object {
        const val DARK_MODE = "darkMode"
    }

    fun isDarkModeEnabled(): Boolean {
        val boolean = sharedPreferences.getBoolean(DARK_MODE, false)
        Timber.d("Darkmode in settings enabled: $boolean")
        return boolean
    }

    fun setDarkModeEnabled(on: Boolean) {
        sharedPreferences.edit {
            putBoolean(DARK_MODE, on)
        }
    }

}