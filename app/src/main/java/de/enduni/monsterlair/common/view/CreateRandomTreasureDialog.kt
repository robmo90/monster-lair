package de.enduni.monsterlair.common.view

import android.app.Activity
import android.net.Uri
import androidx.annotation.StringRes
import androidx.appcompat.app.AlertDialog
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.textfield.TextInputEditText
import de.enduni.monsterlair.R
import de.enduni.monsterlair.common.domain.Level
import de.enduni.monsterlair.common.openCustomTab
import de.enduni.monsterlair.databinding.DialogRandomTreasureBinding
import timber.log.Timber
import java.lang.ref.WeakReference

class CreateRandomTreasureDialog(
    activity: Activity,
    private val listener: Listener
) {

    private val activityRef: WeakReference<Activity> = WeakReference(activity)


    fun show() {
        activityRef.get()?.let { activity ->
            val binding = DialogRandomTreasureBinding.inflate(activity.layoutInflater)

            val dialog = MaterialAlertDialogBuilder(activity, R.style.AlertDialogStyle)
                .setTitle(R.string.create_random_treasure)
                .setView(binding.root)
                .setPositiveButton(
                    android.R.string.ok,
                    null
                )
                .setNeutralButton(R.string.random_encounter_information, null)
                .setNegativeButton(android.R.string.cancel) { _, _ -> }
                .create()

            dialog.setOnShowListener {
                dialog.getButton(AlertDialog.BUTTON_NEUTRAL).setOnClickListener {
                    Uri.parse("https://2e.aonprd.com/Rules.aspx?ID=581")
                        .openCustomTab(dialog.context)
                }
                dialog.getButton(AlertDialog.BUTTON_POSITIVE)?.setOnClickListener {
                    try {
                        validateInput(binding)
                        val level = binding.characterLevelEditText.text.toString()
                            .toInt()
                        val numberOfPlayers = binding.characterNumberEditText.text.toString()
                            .toInt()
                        listener.onCreateClicked(level = level, numberOfPlayers = numberOfPlayers)
                        dialog.dismiss()
                    } catch (ex: Exception) {
                        Timber.d("Not done yet")
                    }

                }
            }

            dialog.show()
        }

    }

    private fun validateInput(binding: DialogRandomTreasureBinding) {
        binding.characterLevelEditText.checkNumbers(R.string.enter_valid_level)
        binding.characterNumberEditText.checkNumbers(R.string.enter_valid_number)

    }

    private fun TextInputEditText.checkNumbers(@StringRes errorStringId: Int) {
        val text = this.text.toString()
        if (text.isBlank() || !IntRange(0, 20).contains(text.toIntOrNull())) {
            this.error = activityRef.get()?.getString(errorStringId)
            throw IllegalStateException("No valid numbers yet")
        }
    }

    interface Listener {

        fun onCreateClicked(level: Level, numberOfPlayers: Int)

    }

}