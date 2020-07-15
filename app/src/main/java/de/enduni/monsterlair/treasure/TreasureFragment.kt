package de.enduni.monsterlair.treasure

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.widget.doAfterTextChanged
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import com.google.android.material.bottomsheet.BottomSheetBehavior
import de.enduni.monsterlair.R
import de.enduni.monsterlair.databinding.FragmentTreasureBinding
import de.enduni.monsterlair.treasure.view.TreasureViewModel
import de.enduni.monsterlair.treasure.view.adapter.TreasureListAdapter
import org.koin.androidx.viewmodel.ext.android.viewModel

class TreasureFragment : Fragment() {

    private lateinit var binding: FragmentTreasureBinding

    private lateinit var listAdapter: TreasureListAdapter

    private val viewModel: TreasureViewModel by viewModel()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return layoutInflater.inflate(R.layout.fragment_treasure, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        binding = FragmentTreasureBinding.bind(view)
        listAdapter = TreasureListAdapter(requireActivity().layoutInflater, viewModel)

        binding.treasureRecyclerView.adapter = listAdapter
        viewModel.treasures.observe(
            viewLifecycleOwner,
            Observer { listAdapter.submitList(it) })

        viewModel.start()
        bindUi()
    }

    private fun bindUi() {
        binding.searchEditText.doAfterTextChanged {
            viewModel.filterByString(it.toString())
        }
        binding.levelSlider.setOnThumbValueChangeListener { _, _, thumbIndex, value ->
            when (thumbIndex) {
                0 -> viewModel.adjustFilterLevelLower(value)
                1 -> viewModel.adjustFilterLevelUpper(value)
            }
        }
        setupBottomSheet()
    }

    private fun setupBottomSheet() {
        val bottomSheetBehavior = BottomSheetBehavior.from(binding.filterBottomSheet)
        bottomSheetBehavior.state = BottomSheetBehavior.STATE_COLLAPSED
        bottomSheetBehavior.peekHeight =
            requireContext().resources.getDimensionPixelSize(R.dimen.bottom_sheet_peek)
    }

}