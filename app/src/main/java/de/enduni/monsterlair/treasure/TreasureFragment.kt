package de.enduni.monsterlair.treasure

import android.app.Activity
import android.os.Bundle
import android.view.View
import androidx.core.widget.doAfterTextChanged
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import com.google.android.material.bottomsheet.BottomSheetDialog
import de.enduni.monsterlair.R
import de.enduni.monsterlair.databinding.BottomsheetLevelBinding
import de.enduni.monsterlair.databinding.BottomsheetSearchBinding
import de.enduni.monsterlair.databinding.FragmentTreasureBinding
import de.enduni.monsterlair.treasure.domain.TreasureFilter
import de.enduni.monsterlair.treasure.view.TreasureViewModel
import de.enduni.monsterlair.treasure.view.adapter.TreasureListAdapter
import org.koin.androidx.viewmodel.ext.android.viewModel

class TreasureFragment : Fragment(R.layout.fragment_treasure) {

    private lateinit var binding: FragmentTreasureBinding

    private lateinit var listAdapter: TreasureListAdapter

    private val viewModel: TreasureViewModel by viewModel()

    private lateinit var levelDialog: LevelRangeBottomSheet

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        binding = FragmentTreasureBinding.bind(view)

        levelDialog = LevelRangeBottomSheet(requireActivity(), viewModel)
        listAdapter = TreasureListAdapter(requireActivity().layoutInflater, viewModel)

        binding.treasureRecyclerView.adapter = listAdapter
        viewModel.treasures.observe(viewLifecycleOwner, Observer { listAdapter.submitList(it) })
        viewModel.filter.observe(viewLifecycleOwner, Observer { adjustUi(it) })

        viewModel.start()
        bindUi()
    }

    private fun adjustUi(filter: TreasureFilter) {
        binding.searchButton.text = if (filter.searchString.isBlank()) {
            "Search"
        } else {
            filter.searchString
        }
        binding.levelButton.text = "${filter.lowerLevel} - ${filter.upperLevel}"
        levelDialog.updateLevelRange(filter.lowerLevel, filter.upperLevel)
    }

    private fun bindUi() {
        binding.searchButton.setOnClickListener { openSearchSheet() }
        binding.levelButton.setOnClickListener { openLevelSheet() }
    }

    private fun openSearchSheet() {
        val binding =
            BottomsheetSearchBinding.inflate(requireActivity().layoutInflater, null, false)
        val bottomSheetDialog = BottomSheetDialog(requireContext())
        binding.searchEditText.doAfterTextChanged { viewModel.filterByString(it.toString()) }
        bottomSheetDialog.setContentView(binding.root)
        bottomSheetDialog.show()
    }

    private fun openLevelSheet() {
        levelDialog.show()
    }


}

class LevelRangeBottomSheet(activity: Activity, private val listener: Listener) :
    BottomSheetDialog(activity) {

    val binding = BottomsheetLevelBinding.inflate(activity.layoutInflater, null, false)

    init {
        setContentView(binding.root)
        binding.levelSlider.setOnThumbValueChangeListener { _, _, thumbIndex, value ->
            when (thumbIndex) {
                0 -> listener.adjustLowerLevel(value)
                1 -> listener.adjustUpperLevel(value)
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

        fun adjustLowerLevel(level: Int)
        fun adjustUpperLevel(level: Int)

    }

}