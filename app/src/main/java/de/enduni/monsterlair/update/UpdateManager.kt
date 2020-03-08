package de.enduni.monsterlair.update

import android.app.Activity
import android.content.Context
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import de.enduni.monsterlair.BuildConfig
import de.enduni.monsterlair.R

class UpdateManager(
    context: Context
) {

    private val sharedPreferences = context.getSharedPreferences(
        "MonsterLair",
        Context.MODE_PRIVATE
    )

    fun showUpdateInformationDialog(activity: Activity) {
        val currentVersion = sharedPreferences.getInt(KEY_BUILD_NUMBER, -1)
        if (currentVersion == BuildConfig.VERSION_CODE) {
            MaterialAlertDialogBuilder(activity)
                .setTitle(activity.getString(R.string.whats_new, BuildConfig.VERSION_NAME))
                .setMessage(R.string.whats_new_8)
                .setPositiveButton(android.R.string.ok) { _, _ -> }
                .create()
                .apply {
                    setOnShowListener {
                        sharedPreferences.edit().putInt(KEY_BUILD_NUMBER, BuildConfig.VERSION_CODE)
                            .apply()
                    }
                }
                .show()

        }

    }

    companion object {

        const val KEY_BUILD_NUMBER = "buildNumber"

    }

}