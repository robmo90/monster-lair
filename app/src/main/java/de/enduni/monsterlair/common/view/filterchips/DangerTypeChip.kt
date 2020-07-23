package de.enduni.monsterlair.common.view.filterchips

import android.app.Activity
import android.content.Context
import android.util.AttributeSet
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.chip.Chip
import com.google.android.material.chip.ChipGroup
import de.enduni.monsterlair.R
import de.enduni.monsterlair.common.filter.DangerTypeFilterStore
import de.enduni.monsterlair.common.getStringRes
import de.enduni.monsterlair.creator.view.DangerType
import de.enduni.monsterlair.databinding.BottomsheetDangerTypesBinding

class DangerTypeChip(context: Context, attributeSet: AttributeSet?) : Chip(context, attributeSet) {

    init {
        this.chipIcon = context.getDrawable(R.drawable.ic_monster_dragon)
    }

    private lateinit var sheet: BottomSheet

    fun setup(activity: Activity, filterStore: DangerTypeFilterStore) {
        sheet = BottomSheet(activity, filterStore)
        this.setOnClickListener { sheet.show() }
    }

    fun update(dangerTypes: List<DangerType>) {
        sheet.updateDangerTypes(dangerTypes)
        text = if (dangerTypes.isEmpty() || dangerTypes.size == 2) {
            context.getString(R.string.danger_type_all)
        } else {
            context.getString(dangerTypes.first().getStringRes())
        }
    }


    class BottomSheet(activity: Activity, private val filterStore: DangerTypeFilterStore) :
        BottomSheetDialog(activity) {

        val binding = BottomsheetDangerTypesBinding.inflate(activity.layoutInflater, null, false)

        init {
            setContentView(binding.root)
            binding.dangerTypeChips.buildDangerTypeChips(emptyList(), filterStore)
        }

        fun updateDangerTypes(dangerTypes: List<DangerType>) {
            binding.dangerTypeChips.buildDangerTypeChips(emptyList(), filterStore)
        }


        private fun ChipGroup.buildDangerTypeChips(
            checkedDangers: List<DangerType> = emptyList(),
            filterStore: DangerTypeFilterStore
        ) {
            this.removeAllViews()
            DangerType.values().forEach { dangerType ->
                val chip = SelectionChip(
                    this.context,
                    this.context.getString(dangerType.getStringRes()),
                    checkedDangers.contains(dangerType),
                    { filterStore.addDangerType(dangerType) },
                    { filterStore.removeDangerType(dangerType) }
                )
                this.addView(chip)
            }
        }
    }

}