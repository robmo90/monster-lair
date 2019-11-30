package de.enduni.monsterlair.encounters.creator.view

import android.app.Activity
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.textfield.TextInputEditText
import de.enduni.monsterlair.R
import de.enduni.monsterlair.common.setTextIfNotFocused

object SaveDialog {

    fun show(context: Activity, name: String, saveCallback: (String) -> Unit) {
        val view = context.layoutInflater.inflate(R.layout.dialog_save_encounter, null)
        val editText = view.findViewById<TextInputEditText>(R.id.name_edit_text)
        editText.setTextIfNotFocused(name)
        MaterialAlertDialogBuilder(context)
            .setView(view)
            .setPositiveButton("Save") { dialog, _ ->
                val text = editText.text.toString()
                val givenName = if (text.isEmpty()) "Encounter" else text
                saveCallback.invoke(givenName)
                dialog.dismiss()
            }
            .setNegativeButton("Cancel") { dialog, _ ->
                dialog.dismiss()
            }
            .create()
            .show()
    }

}