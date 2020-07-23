package de.enduni.monsterlair.creator.view

import android.app.Activity
import androidx.appcompat.app.AlertDialog
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import de.enduni.monsterlair.R
import de.enduni.monsterlair.common.domain.RandomEncounterTemplate

class RandomEncounterDialog {

    fun show(activity: Activity, randomEncounterCallback: (RandomEncounterTemplate) -> Unit) {
        MaterialAlertDialogBuilder(activity)
            .setTitle(R.string.random_encounter_title)
            .setItems(R.array.random_encounter_templates) { dialog, which ->
                randomEncounterCallback.invoke(RandomEncounterTemplate.values()[which])
            }
            .setNegativeButton(android.R.string.cancel) { _, _ -> }
            .setNeutralButton(R.string.random_encounter_information, null)
            .create()
            .apply {
                setOnShowListener {
                    getButton(AlertDialog.BUTTON_NEUTRAL).setOnClickListener {
                        MaterialAlertDialogBuilder(activity)
                            .setTitle(R.string.random_encounter_information_title)
                            .setMessage(R.string.random_encounter_message)
                            .setPositiveButton(android.R.string.ok) { _, _ -> }
                            .show()
                    }
                }
                show()
            }
    }


}