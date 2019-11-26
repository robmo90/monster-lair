package de.enduni.monsterlair.encounters.creator.view

import android.app.Activity
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.textfield.TextInputEditText
import de.enduni.monsterlair.R

object SaveDialog {

    fun show(context: Activity, saveCallback: (String) -> Unit) {
        val view = context.layoutInflater.inflate(R.layout.dialog_save_encounter, null)
        val editText = view.findViewById<TextInputEditText>(R.id.name_edit_text)
        MaterialAlertDialogBuilder(context)
            .setView(view)
            .setPositiveButton("Save") { dialog, _ ->
                val text = editText.text.toString()
                val name = if (text.isEmpty()) "Encounter" else text
                saveCallback.invoke(name)
                dialog.dismiss()
            }
            .setNegativeButton("Cancel") { dialog, _ ->
                dialog.dismiss()
            }
            .create()
            .show()
    }

}