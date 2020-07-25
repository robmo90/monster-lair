package de.enduni.monsterlair.common.sources

import android.app.Activity
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import de.enduni.monsterlair.R
import de.enduni.monsterlair.common.domain.Source

class SelectSourcesDialog {


    fun show(activity: Activity, currentSources: List<Source>, listener: Listener) {
        MaterialAlertDialogBuilder(activity, R.style.AlertDialogStyle)
            .setTitle(R.string.sources_dialog)
            .setMultiChoiceItems(
                R.array.sources,
                Source.values().map { currentSources.contains(it) }.toBooleanArray()
            ) { _, which, isChecked ->
                val selectedSource = Source.values()[which]
                if (isChecked) {
                    listener.addSource(selectedSource)
                } else {
                    listener.removeSource(selectedSource)
                }
            }
            .setPositiveButton(android.R.string.ok) { _, _ -> }
            .create()
            .show()
    }

    interface Listener {

        fun addSource(source: Source)
        fun removeSource(source: Source)

    }


}