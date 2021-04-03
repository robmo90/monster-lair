package de.enduni.monsterlair.update

import android.app.Activity
import android.content.SharedPreferences
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import de.enduni.monsterlair.BuildConfig
import de.enduni.monsterlair.R
import de.enduni.monsterlair.common.domain.Source
import de.enduni.monsterlair.common.sources.SourceManager

class UpdateManager(
    private val sharedPreferences: SharedPreferences,
    private val sourceManager: SourceManager
) {

    val savedVersion: Int
        get() = sharedPreferences.getInt(KEY_BUILD_NUMBER, -1)

    fun showUpdateInformationDialog(activity: Activity) {
        val currentVersion = BuildConfig.VERSION_CODE

        if (savedVersion <= 15 && currentVersion >= 16) {
            showUpdateDialog(activity, currentVersion, R.string.whats_new_16)
        } else {
            sharedPreferences.edit().putInt(KEY_BUILD_NUMBER, currentVersion)
                .apply()
        }
        if (savedVersion <= 16) {
            if (sourceManager.sources.size == Source.values().size - 1) {
                sourceManager.sources = Source.values().asList()
            }
        }
    }

    private fun showUpdateDialog(
        activity: Activity,
        currentVersion: Int,
        whatsNewText: Int
    ) {
        MaterialAlertDialogBuilder(activity, R.style.AlertDialogStyle)
            .setTitle(activity.getString(R.string.whats_new, BuildConfig.VERSION_NAME))
            .setMessage(whatsNewText)
            .setPositiveButton(android.R.string.ok) { _, _ -> }
            .create()
            .apply {
                setOnShowListener {
                    sharedPreferences.edit().putInt(KEY_BUILD_NUMBER, currentVersion)
                        .apply()
                }
            }
            .show()
    }

    companion object {

        const val KEY_BUILD_NUMBER = "buildNumber"

    }

}