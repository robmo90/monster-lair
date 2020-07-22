package de.enduni.monsterlair.common.view.filterchips

import android.content.Context
import com.google.android.material.chip.Chip
import com.google.android.material.chip.ChipDrawable
import de.enduni.monsterlair.R

class SelectionChip(
    context: Context,
    label: String,
    checked: Boolean,
    onCheckAction: () -> Unit,
    onUncheckAction: () -> Unit
) : Chip(context) {


    init {
        val chipDrawable = ChipDrawable.createFromAttributes(
            this.context,
            null,
            0,
            R.style.Widget_MaterialComponents_Chip_Choice
        )
        setChipDrawable(chipDrawable)
        text = label
        isChecked = checked
        setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) {
                onCheckAction.invoke()
            } else {
                onUncheckAction.invoke()
            }
        }
    }


}