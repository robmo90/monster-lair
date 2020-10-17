package de.enduni.monsterlair.common.view

import android.app.Activity
import android.content.Context
import android.view.inputmethod.InputMethodManager
import androidx.annotation.StringRes
import androidx.appcompat.app.AlertDialog
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.textfield.TextInputEditText
import de.enduni.monsterlair.R
import de.enduni.monsterlair.common.getStringRes
import de.enduni.monsterlair.common.setTextIfNotFocused
import de.enduni.monsterlair.databinding.DialogCreateEncounterBinding
import de.enduni.monsterlair.encounters.domain.model.EncounterDifficulty
import java.lang.ref.WeakReference

class EncounterSettingDialog(
    private val purpose: Purpose,
    private val settings: Settings? = null,
    activity: Activity
) {

    private val activityRef: WeakReference<Activity> = WeakReference(activity)

    fun show(resultCallback: (Settings) -> Unit) {
        activityRef.get()?.let { activity ->
            val binding = DialogCreateEncounterBinding.inflate(activity.layoutInflater)
            setupDifficultySelect(activity, binding)

            settings?.run {
                binding.encounterNameEditText.setTextIfNotFocused(encounterName)
                binding.characterLevelEditText.setTextIfNotFocused(encounterLevel)
                binding.characterNumberEditText.setTextIfNotFocused(numberOfPlayers)
                binding.difficultySelect.setText(encounterDifficulty.getStringRes())
                binding.withoutProficiencyCheckbox.isChecked = useProficiencyWithoutLevel
                binding.notesEditText.setTextIfNotFocused(notes)
            }

            val dialog = MaterialAlertDialogBuilder(activity, R.style.AlertDialogStyle)
                .setView(binding.root)
                .setTitle(
                    if (purpose == Purpose.CREATE) {
                        R.string.create_encounter
                    } else {
                        R.string.edit_encounter
                    }
                )
                .setPositiveButton(
                    if (purpose == Purpose.CREATE) {
                        R.string.create_encounter_button
                    } else {
                        R.string.save_encounter
                    }, null
                )
                .setNegativeButton(android.R.string.cancel) { _, _ -> }
                .create()

            dialog.setOnShowListener {
                dialog.getButton(AlertDialog.BUTTON_POSITIVE)?.setOnClickListener {
                    try {
                        validateInput(binding)
                        resultCallback.invoke(
                            Settings(
                                encounterName = binding.encounterNameEditText.text.toString(),
                                numberOfPlayers = binding.characterNumberEditText.text.toString()
                                    .toInt(),
                                encounterLevel = binding.characterLevelEditText.text.toString()
                                    .toInt(),
                                encounterDifficulty = getDifficulty(activity, binding),
                                useProficiencyWithoutLevel = binding.withoutProficiencyCheckbox.isChecked,
                                notes = binding.notesEditText.text.toString()
                            )
                        )
                        dialog.dismiss()
                    } catch (e: IllegalStateException) {

                    }
                }
            }


            dialog.show()
        }
    }

    private fun validateInput(binding: DialogCreateEncounterBinding) {
        binding.characterLevelEditText.checkNumbers(R.string.enter_valid_level)
        binding.characterNumberEditText.checkNumbers(R.string.enter_valid_number)

    }

    private fun setupDifficultySelect(context: Context, binding: DialogCreateEncounterBinding) {
        val choices = context.resources.getStringArray(R.array.difficulty_choices)
        MaterialSpinnerAdapter(
            context,
            R.layout.view_spinner_item,
            choices
        ).also { adapter ->
            binding.difficultySelect.setAdapter(adapter)
        }
        binding.difficultySelect.setOnFocusChangeListener { v, hasFocus ->
            if (hasFocus) {
                context.getSystemService(InputMethodManager::class.java)?.apply {
                    hideSoftInputFromWindow(v.windowToken, 0)
                }
            }
        }
    }

    private fun getDifficulty(
        context: Context,
        binding: DialogCreateEncounterBinding
    ): EncounterDifficulty {
        val choices = context.resources.getStringArray(R.array.difficulty_choices)
        return when (choices.indexOf(binding.difficultySelect.text.toString())) {
            0 -> EncounterDifficulty.TRIVIAL
            1 -> EncounterDifficulty.LOW
            2 -> EncounterDifficulty.MODERATE
            3 -> EncounterDifficulty.SEVERE
            4 -> EncounterDifficulty.EXTREME
            else -> throw IllegalStateException()
        }
    }


    private fun TextInputEditText.checkNumbers(@StringRes errorStringId: Int) {
        val text = this.text.toString()
        if (text.isBlank() || !IntRange(0, 20).contains(text.toIntOrNull())) {
            this.error = activityRef.get()?.getString(errorStringId)
            throw IllegalStateException("No valid numbers yet")
        }
    }

    data class Settings(
        val encounterName: String,
        val numberOfPlayers: Int,
        val encounterLevel: Int,
        val encounterDifficulty: EncounterDifficulty,
        val useProficiencyWithoutLevel: Boolean,
        val notes: String
    )

    enum class Purpose {
        EDIT,
        CREATE
    }
}