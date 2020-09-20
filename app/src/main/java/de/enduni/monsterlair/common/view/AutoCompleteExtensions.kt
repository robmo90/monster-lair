package de.enduni.monsterlair.common.view

import android.app.Activity
import android.text.SpannableStringBuilder
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.AutoCompleteTextView
import android.widget.TextView
import de.enduni.monsterlair.R
import de.enduni.monsterlair.common.domain.*
import de.enduni.monsterlair.common.filter.*


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
    setupSimpleSelect(categories) { index -> filterStore.addCategory(TreasureCategory.values()[index]) }
}

fun AutoCompleteTextView.setupTraitsSelect(
    traits: List<Trait>,
    traitFilterStore: TraitFilterStore
) {
    setupSimpleSelect(traits.toTypedArray()) { index -> traitFilterStore.addTrait(traits[index]) }
}

fun AutoCompleteTextView.setupMonsterTypeSelect(
    filterStore: MonsterTypeFilterStore
) {
    val types = context.resources.getStringArray(R.array.type_choices)
    setupSimpleSelect(types) { index -> filterStore.addType(MonsterType.values()[index]) }
}

fun AutoCompleteTextView.setupAlignmentSelect(
    filterStore: AlignmentFilterStore
) {
    val types = context.resources.getStringArray(R.array.alignments)
    setupDropdownSelect(types) { index -> filterStore.addAlignment(Alignment.values()[index]) }
}

fun AutoCompleteTextView.setupSizeSelect(
    filterStore: SizeFilterStore
) {
    val types = context.resources.getStringArray(R.array.sizes)
    setupDropdownSelect(types) { index -> filterStore.addSize(Size.values()[index]) }
}

fun AutoCompleteTextView.setupSimpleSelect(
    list: Array<String>,
    onClickListener: (index: Int) -> Unit
) {
    val adapter = object : ArrayAdapter<String>(
        context,
        R.layout.view_spinner_item,
        list
    ) {}
    setAdapter(adapter)
    onItemClickListener = AdapterView.OnItemClickListener { _, view, _, _ ->
        onClickListener.invoke(list.indexOf((view as TextView).text))
        text = SpannableStringBuilder("")

        val imm = context.getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.toggleSoftInput(InputMethodManager.HIDE_IMPLICIT_ONLY, 0)
    }
}

fun AutoCompleteTextView.setupDropdown(
    list: Array<String>,
    onClickListener: (index: Int) -> Unit
) {
    val adapter = MaterialSpinnerAdapter(
        context,
        R.layout.view_spinner_item,
        list
    )
    setAdapter(adapter)
    onItemClickListener = AdapterView.OnItemClickListener { _, view, _, _ ->
        onClickListener.invoke(list.indexOf((view as TextView).text))

        val imm = context.getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.toggleSoftInput(InputMethodManager.HIDE_IMPLICIT_ONLY, 0)
    }
}

fun AutoCompleteTextView.setupDropdownSelect(
    list: Array<String>,
    onClickListener: (index: Int) -> Unit
) {
    val adapter = MaterialSpinnerAdapter(
        context,
        R.layout.view_spinner_item,
        list
    )
    setAdapter(adapter)
    onItemClickListener = AdapterView.OnItemClickListener { _, view, _, _ ->
        onClickListener.invoke(list.indexOf((view as TextView).text))
    }
}
