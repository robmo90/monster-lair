package de.enduni.monsterlair.creator.view

import android.content.Context
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import de.enduni.monsterlair.R
import de.enduni.monsterlair.common.domain.Strength

class AdjustMonsterStrengthDialog {

    fun show(
        context: Context,
        monsterId: String,
        currentStrength: Strength,
        listener: Listener
    ) {
        MaterialAlertDialogBuilder(context, R.style.AlertDialogStyle)
            .setTitle(R.string.adjust_monster_strength)
            .setItems(R.array.strengths) { dialog, which ->
                listener.onStrengthChosen(monsterId, currentStrength, Strength.values()[which])
            }
            .setNegativeButton(android.R.string.cancel) { _, _ -> }
            .create()
            .show()
    }

    interface Listener {

        fun onStrengthChosen(id: String, currentStrength: Strength, targetStrength: Strength)

    }


}