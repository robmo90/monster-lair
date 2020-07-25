package de.enduni.monsterlair.update

import android.app.Activity
import android.content.SharedPreferences
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import de.enduni.monsterlair.BuildConfig
import de.enduni.monsterlair.R

class UpdateManager(
    private val sharedPreferences: SharedPreferences
) {

    val savedVersion: Int
        get() = sharedPreferences.getInt(KEY_BUILD_NUMBER, -1)

    fun showUpdateInformationDialog(activity: Activity) {
        val currentVersion = BuildConfig.VERSION_CODE

        if (savedVersion < 8 && currentVersion < 10) {
            showUpdateDialog(activity, currentVersion, R.string.whats_new_8)
        }
        if (savedVersion < 10 && currentVersion >= 10) {
            showUpdateDialog(activity, currentVersion, R.string.whats_new_10)
        }
        if (savedVersion < 11 && currentVersion >= 11) {
            showUpdateDialog(activity, currentVersion, R.string.whats_new_10)
        }
    }

    private fun showUpdateDialog(
        activity: Activity,
        currentVersion: Int,
        whatsNewText: Int
    ) {
        MaterialAlertDialogBuilder(activity)
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