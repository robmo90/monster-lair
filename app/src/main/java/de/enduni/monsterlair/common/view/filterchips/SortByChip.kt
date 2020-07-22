package de.enduni.monsterlair.common.view.filterchips

import android.app.Activity
import android.content.Context
import android.util.AttributeSet
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.chip.Chip
import com.google.android.material.chip.ChipDrawable
import com.google.android.material.chip.ChipGroup
import de.enduni.monsterlair.R
import de.enduni.monsterlair.common.domain.SortBy
import de.enduni.monsterlair.common.filter.SortByFilterStore
import de.enduni.monsterlair.common.getStringRes
import de.enduni.monsterlair.databinding.BottomsheetSortByBinding

class SortByChip(context: Context, attributeSet: AttributeSet?) : Chip(context, attributeSet) {

    init {
        this.chipIcon = context.getDrawable(R.drawable.ic_sort)
    }

    private lateinit var sheet: BottomSheet

    fun setup(activity: Activity, filterStore: SortByFilterStore) {
        sheet = BottomSheet(activity, filterStore)
        this.setOnClickListener { sheet.show() }
    }

    fun update(sortBy: SortBy) {
        sheet.updateSortBy(sortBy)
        text = context.getString(sortBy.getStringRes())
    }


    class BottomSheet(activity: Activity, private val filterStore: SortByFilterStore) :
        BottomSheetDialog(activity) {

        val binding = BottomsheetSortByBinding.inflate(activity.layoutInflater, null, false)

        init {
            setContentView(binding.root)
            binding.sortByChips.buildSortByChips()
        }

        fun updateSortBy(sortBy: SortBy) {
            binding.sortByChips.buildSortByChips(sortBy)
        }


        private fun ChipGroup.buildSortByChips(checkedSortBy: SortBy = SortBy.NAME) {
            this.isSingleSelection = true
            this.removeAllViews()
            SortBy.values().forEach { sortBy ->
                val chip = Chip(this.context)
                val chipDrawable = ChipDrawable.createFromAttributes(
                    this.context,
                    null,
                    0,
                    R.style.Widget_MaterialComponents_Chip_Choice
                )
                chip.setChipDrawable(chipDrawable)
                chip.text = this.context.getString(sortBy.getStringRes())
                chip.isChecked = checkedSortBy == sortBy
                chip.setOnCheckedChangeListener { _, isChecked ->
                    if (isChecked) {
                        filterStore.setSortBy(sortBy)
                    }
                }



                this.addView(chip)
            }
        }

    }

}