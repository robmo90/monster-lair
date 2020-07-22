package de.enduni.monsterlair.common.view.filterchips

import android.content.Context
import com.google.android.material.chip.Chip
import com.google.android.material.chip.ChipDrawable
import de.enduni.monsterlair.R

class RemovableFilterChip(
    context: Context,
    label: String,
    removeAction: () -> Unit
) : Chip(context) {


    init {
        setChipDrawable(
            ChipDrawable.createFromAttributes(
                context,
                null,
                0,
                R.style.Widget_MaterialComponents_Chip_Entry
            )
        )
        text = label
        isCheckable = false
        setOnCloseIconClickListener { removeAction.invoke() }
    }


}