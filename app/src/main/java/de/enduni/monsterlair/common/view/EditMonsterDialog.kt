package de.enduni.monsterlair.common.view

import android.app.Activity
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import de.enduni.monsterlair.R
import java.lang.ref.WeakReference

class EditMonsterDialog(
    activity: Activity,
    private val id: String,
    private val name: String,
    private val onEditMonsterClickListener: OnEditMonsterClickListener
) {

    private val activityRef: WeakReference<Activity> = WeakReference(activity)

    fun show() {
        activityRef.get()?.let { activity ->
            MaterialAlertDialogBuilder(activity)
                .setTitle(name)
                .setItems(R.array.custom_monster_actions) { dialog, which ->
                    when (which) {
                        0 -> onEditMonsterClickListener.onEditClicked(id)
                        1 -> onEditMonsterClickListener.onDeleteClicked(id)
                    }
                }
                .setNegativeButton(android.R.string.cancel) { _, _ -> }
                .create()
                .show()

        }
    }


    interface OnEditMonsterClickListener {
        fun onEditClicked(id: String)
        fun onDeleteClicked(id: String)
    }

}