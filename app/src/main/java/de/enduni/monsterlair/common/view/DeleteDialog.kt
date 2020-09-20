package de.enduni.monsterlair.common.view

import android.app.Activity
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import de.enduni.monsterlair.R
import java.lang.ref.WeakReference

class DeleteDialog(
    activity: Activity,
    private val id: String,
    private val name: String,
    private val deleteConfirmationListener: (String) -> Unit
) {

    private val activityRef: WeakReference<Activity> = WeakReference(activity)

    fun show() {
        activityRef.get()?.let { activity ->
            MaterialAlertDialogBuilder(activity, R.style.AlertDialogStyle)
                .setTitle(activity.getString(R.string.custom_monster_delete_title, name))
                .setMessage(activity.getString(R.string.custom_monster_delete_message, name))
                .setPositiveButton(R.string.custom_monster_delete_button) { _, _ ->
                    deleteConfirmationListener.invoke(
                        id
                    )
                }
                .setNegativeButton(android.R.string.cancel) { _, _ -> }
                .create()
                .show()

        }
    }

}