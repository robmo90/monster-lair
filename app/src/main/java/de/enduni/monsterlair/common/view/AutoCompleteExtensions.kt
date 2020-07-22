package de.enduni.monsterlair.common.view

import android.text.SpannableStringBuilder
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.AutoCompleteTextView
import android.widget.TextView
import de.enduni.monsterlair.R
import de.enduni.monsterlair.common.domain.TreasureCategory


fun AutoCompleteTextView.adjustBottomSheetPadding(rootView: View) {
    setOnDismissListener {
        rootView.setPadding(
            0,
            0,
            0,
            context.resources.getDimensionPixelSize(R.dimen.bottom_sheet_bottom_padding)
        )
    }
    setOnFocusChangeListener { v, hasFocus ->
        if (hasFocus) {
            rootView.setPadding(
                0,
                0,
                0,
                context.resources.getDimensionPixelSize(R.dimen.bottom_sheet_adjustment)
            )
        }
    }
}


fun AutoCompleteTextView.setupTreasureCategorySelect(
    addAction: (TreasureCategory) -> Unit
) {
    val categories = context.resources.getStringArray(R.array.treasure_categories)
    val adapter = object : ArrayAdapter<String>(
        context,
        R.layout.view_spinner_item,
        categories
    ) {}
    setAdapter(adapter)
    onItemClickListener = AdapterView.OnItemClickListener { _, view, position, _ ->
        addAction.invoke(TreasureCategory.values()[categories.indexOf((view as TextView).text)])
        text = SpannableStringBuilder("")
    }
}

fun AutoCompleteTextView.setupTraitsSelect(
    traits: List<String>,
    addAction: (String) -> Unit
) {
    val adapter = object : ArrayAdapter<String>(
        context,
        R.layout.view_spinner_item,
        traits
    ) {}
    setAdapter(adapter)
    onItemClickListener = AdapterView.OnItemClickListener { _, view, position, _ ->
        addAction.invoke((view as TextView).text as String)
        text = SpannableStringBuilder("")
    }
}