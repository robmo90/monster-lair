package de.enduni.monsterlair.common.view

import android.text.SpannableStringBuilder
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.AutoCompleteTextView
import android.widget.TextView
import de.enduni.monsterlair.R
import de.enduni.monsterlair.common.domain.Trait
import de.enduni.monsterlair.common.domain.TreasureCategory
import de.enduni.monsterlair.common.filter.TraitFilterStore
import de.enduni.monsterlair.common.filter.TreasureCategoryFilterStore


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
    filterStore: TreasureCategoryFilterStore
) {
    val categories = context.resources.getStringArray(R.array.treasure_categories)
    val adapter = object : ArrayAdapter<String>(
        context,
        R.layout.view_spinner_item,
        categories
    ) {}
    setAdapter(adapter)
    onItemClickListener = AdapterView.OnItemClickListener { _, view, position, _ ->
        filterStore.addCategory(TreasureCategory.values()[categories.indexOf((view as TextView).text)])
        text = SpannableStringBuilder("")
    }
}

fun AutoCompleteTextView.setupTraitsSelect(
    traits: List<String>,
    traitFilterStore: TraitFilterStore
) {
    val adapter = object : ArrayAdapter<String>(
        context,
        R.layout.view_spinner_item,
        traits
    ) {}
    setAdapter(adapter)
    onItemClickListener = AdapterView.OnItemClickListener { _, view, position, _ ->
        traitFilterStore.addTrait((view as TextView).text as Trait)
        text = SpannableStringBuilder("")
    }
}