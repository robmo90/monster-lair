package de.enduni.monsterlair.common.view.filterchips

import android.app.Activity
import android.content.Context
import android.util.AttributeSet
import androidx.core.widget.doAfterTextChanged
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.chip.Chip
import de.enduni.monsterlair.R
import de.enduni.monsterlair.common.setTextIfNotFocused
import de.enduni.monsterlair.databinding.BottomsheetSearchBinding

class SearchChip(context: Context, attributeSet: AttributeSet?) : Chip(context, attributeSet) {

    init {
        this.text = context.getString(R.string.search)
        this.chipIcon = context.getDrawable(R.drawable.ic_search)
    }

    private lateinit var searchSheet: BottomSheet

    fun setup(activity: Activity, listener: BottomSheet.Listener) {
        searchSheet = BottomSheet(activity, listener)
        this.setOnClickListener { searchSheet.show() }
    }

    fun update(searchString: String) {
        searchSheet.updateSearch(searchString)
        text = if (searchString.isBlank()) {
            context.getString(R.string.search)
        } else {
            searchString
        }
    }


    class BottomSheet(activity: Activity, private val listener: Listener) :
        BottomSheetDialog(activity) {

        val binding = BottomsheetSearchBinding.inflate(activity.layoutInflater, null, false)

        init {
            setContentView(binding.root)
            binding.searchEditText.doAfterTextChanged { listener.updateSearch(it.toString()) }
        }

        fun updateSearch(searchString: String) {
            binding.searchEditText.setTextIfNotFocused(searchString)
        }

        interface Listener {

            fun updateSearch(searchString: String)

        }

    }

}