package de.enduni.monsterlair.common.view.filterchips

import android.app.Activity
import android.content.Context
import android.util.AttributeSet
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.chip.Chip
import de.enduni.monsterlair.R
import de.enduni.monsterlair.databinding.BottomsheetLevelBinding

class LevelRangeChip(context: Context, attributeSet: AttributeSet?) : Chip(context, attributeSet) {

    init {
        this.text = context.getString(R.string.level_range_values, 0, 25)
        this.chipIcon = context.getDrawable(R.drawable.ic_filter_list)
    }

    private lateinit var sheet: BottomSheet

    fun setup(
        activity: Activity,
        listener: BottomSheet.Listener,
        lowerLevel: Int = 0,
        upperLevel: Int = 25
    ) {
        sheet = BottomSheet(activity, listener, lowerLevel, upperLevel)
        this.setOnClickListener { sheet.show() }
    }

    fun update(lowerLevel: Int, upperLevel: Int) {
        sheet.updateLevelRange(lowerLevel, upperLevel)
        text = context.getString(R.string.level_range_values, lowerLevel, upperLevel)
    }


    class BottomSheet(
        activity: Activity,
        private val listener: Listener,
        lowerLevel: Int,
        upperLevel: Int
    ) : BottomSheetDialog(activity) {

        val binding = BottomsheetLevelBinding.inflate(activity.layoutInflater, null, false)

        init {
            setContentView(binding.root)
            binding.levelSlider.min = lowerLevel
            binding.levelSlider.max = upperLevel
            binding.levelSlider.setOnThumbValueChangeListener { _, _, thumbIndex, value ->
                when (thumbIndex) {
                    0 -> listener.setLowerLevel(value)
                    1 -> listener.setUpperLevel(value)
                }
            }
        }

        fun updateLevelRange(lowerLevel: Int, upperLevel: Int) {
            binding.levelSliderLabel.text = context.getString(
                R.string.monster_level_range_values,
                lowerLevel,
                upperLevel
            )
            binding.levelSlider.getThumb(0).value = lowerLevel
            binding.levelSlider.getThumb(1).value = upperLevel
        }

        interface Listener {

            fun setLowerLevel(level: Int)
            fun setUpperLevel(level: Int)

        }

    }

}