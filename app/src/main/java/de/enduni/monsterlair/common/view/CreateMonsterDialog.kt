package de.enduni.monsterlair.common.view

import android.app.Activity
import android.content.Context
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.textfield.TextInputEditText
import de.enduni.monsterlair.R
import de.enduni.monsterlair.common.domain.*
import de.enduni.monsterlair.common.getStringRes
import de.enduni.monsterlair.common.setTextIfNotFocused
import de.enduni.monsterlair.databinding.DialogCreateMonsterBinding
import de.enduni.monsterlair.monsters.domain.Monster
import timber.log.Timber
import java.lang.ref.WeakReference
import java.util.*

class CreateMonsterDialog(
    activity: Activity,
    private val onSaveClickedListener: OnSaveClickedListener,
    private val monster: Monster? = null
) {

    private val activityRef: WeakReference<Activity> = WeakReference(activity)


    fun show() {
        activityRef.get()?.let { activity ->
            val binding = DialogCreateMonsterBinding.inflate(activity.layoutInflater)
            setupMonsterTypeSelect(activity, binding)

            val edit = monster != null
            monster?.let {
                binding.monsterNameEditText.setTextIfNotFocused(monster.name)
                binding.monsterLevelEditText.setTextIfNotFocused(monster.level)
                binding.monsterFamilyEditText.setTextIfNotFocused(monster.family)
                binding.monsterTypeSelect.setText(monster.type.getStringRes())
            }

            val dialog = MaterialAlertDialogBuilder(activity, R.style.AlertDialogStyle)
                .setTitle(if (!edit) R.string.custom_monster_create_title else R.string.custom_monster_edit_title)
                .setView(binding.root)
                .setPositiveButton(
                    (if (!edit) R.string.custom_monster_create_button else R.string.custom_monster_edit_button),
                    null
                )
                .setNegativeButton(android.R.string.cancel) { _, _ -> }
                .create()

            dialog.setOnShowListener {
                dialog.getButton(AlertDialog.BUTTON_POSITIVE)?.setOnClickListener {
                    try {
                        val levelOk = binding.monsterLevelEditText.checkLevel()
                        val nameOk = binding.monsterNameEditText.checkName()
                        val type = getMonsterType(activity, binding)
                        if (!levelOk || !nameOk || type == null) {
                            return@setOnClickListener
                        }
                        val name = binding.monsterNameEditText.text.toString().capitalize()
                        var family = binding.monsterFamilyEditText.text.toString()
                        if (family.isBlank()) {
                            family = "—"
                        }
                        onSaveClickedListener.onSaveClicked(
                            Monster(
                                name = name,
                                family = family,
                                level = binding.monsterLevelEditText.text.toString().toInt(),
                                type = type,
                                source = CustomMonster.SOURCE,
                                id = monster?.id ?: UUID.randomUUID().toString(),
                                alignment = Alignment.NEUTRAL,
                                size = Size.MEDIUM,
                                sourceType = Source.CUSTOM,
                                url = "",
                                traits = emptyList(),
                                rarity = Rarity.COMMON,
                                description = ""
                            )
                        )
                        Toast.makeText(
                            activity,
                            activity.getString(R.string.custom_monster_save_confirmation, name),
                            Toast.LENGTH_SHORT
                        ).show()
                        dialog.dismiss()
                    } catch (ex: IllegalStateException) {
                        Timber.d("Not done yet")
                    }

                }
            }

            dialog.show()
        }

    }

    private fun setupMonsterTypeSelect(context: Context, binding: DialogCreateMonsterBinding) {
        val choices = context.resources.getStringArray(R.array.type_choices)
        MaterialSpinnerAdapter(
            context,
            R.layout.view_spinner_item,
            choices
        ).also { adapter ->
            binding.monsterTypeSelect.setAdapter(adapter)
        }
        binding.monsterTypeSelect.setOnFocusChangeListener { v, hasFocus ->
            if (hasFocus) {
                context.getSystemService(InputMethodManager::class.java)?.apply {
                    hideSoftInputFromWindow(v.windowToken, 0)
                }
            }
        }
    }

    private fun getMonsterType(
        context: Context,
        binding: DialogCreateMonsterBinding
    ): MonsterType? {
        val choices = context.resources.getStringArray(R.array.type_choices)
        val index = choices.indexOf(binding.monsterTypeSelect.text.toString())
        val type = MonsterType.values().getOrNull(index)
        if (type == null) {
            binding.monsterTypeSelect.error =
                activityRef.get()?.getString(R.string.custom_monster_create_wrong_level)
        }
        return type

    }

    private fun TextInputEditText.checkLevel(): Boolean {
        val text = this.text.toString()
        return if (text.isBlank() || !IntRange(-1, 25).contains(text.toIntOrNull())) {
            this.error = activityRef.get()?.getString(R.string.custom_monster_create_wrong_level)
            false
        } else {
            true
        }
    }

    private fun TextInputEditText.checkName(): Boolean {
        val text = this.text.toString()
        return if (text.isBlank()) {
            this.error = activityRef.get()?.getString(R.string.custom_monster_create_empty_name)
            false
        } else {
            true
        }
    }

    interface OnSaveClickedListener {

        fun onSaveClicked(monster: Monster)

    }

}