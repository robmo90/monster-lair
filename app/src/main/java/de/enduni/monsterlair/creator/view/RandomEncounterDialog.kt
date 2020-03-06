package de.enduni.monsterlair.creator.view

import android.app.Activity
import androidx.appcompat.app.AlertDialog
import de.enduni.monsterlair.R
import de.enduni.monsterlair.common.domain.RandomEncounter

class RandomEncounterDialog {

    fun show(activity: Activity, randomEncounterCallback: (RandomEncounter) -> Unit) {
        AlertDialog.Builder(activity)
            .setTitle(R.string.random_encounter_title)
            .setItems(R.array.random_encounter_templates) { dialog, which ->
                randomEncounterCallback.invoke(RandomEncounter.values()[which])
            }
            .setNegativeButton(android.R.string.cancel) { _, _ -> }
            .create()
            .show()
    }


}