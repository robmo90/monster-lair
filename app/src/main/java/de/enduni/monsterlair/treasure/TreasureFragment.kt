package de.enduni.monsterlair.treasure

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import de.enduni.monsterlair.R
import de.enduni.monsterlair.common.view.addGoldCostChips
import de.enduni.monsterlair.common.view.addRarityChips
import de.enduni.monsterlair.common.view.addTraitChips
import de.enduni.monsterlair.common.view.addTreasureCategoryChips
import de.enduni.monsterlair.databinding.FragmentTreasureBinding
import de.enduni.monsterlair.treasure.domain.TreasureFilter
import de.enduni.monsterlair.treasure.view.TreasureViewModel
import de.enduni.monsterlair.treasure.view.adapter.TreasureListAdapter
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.koin.androidx.viewmodel.ext.android.viewModel

@ExperimentalCoroutinesApi
class TreasureFragment : Fragment(R.layout.fragment_treasure) {

    private lateinit var binding: FragmentTreasureBinding

    private lateinit var listAdapter: TreasureListAdapter

    private val viewModel: TreasureViewModel by viewModel()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        binding = FragmentTreasureBinding.bind(view)

        listAdapter = TreasureListAdapter(requireActivity().layoutInflater, viewModel)

        binding.treasureRecyclerView.adapter = listAdapter
        viewModel.treasures.observe(viewLifecycleOwner, Observer { listAdapter.submitList(it) })
        viewModel.filter.observe(viewLifecycleOwner, Observer { updateUi(it) })

        bindUi()
    }

    private fun updateUi(filter: TreasureFilter) {
        binding.levelButton.update(filter.lowerLevel, filter.upperLevel)
        binding.sortButton.update(filter.sortBy)
        binding.searchButton.update(filter.searchTerm)
        binding.additionalFilterChips.removeAllViews()
        binding.additionalFilterChips.addTreasureCategoryChips(
            filter.categories,
            filterStore = viewModel.filterStore
        )
        binding.additionalFilterChips.addTraitChips(
            filter.traits,
            filterStore = viewModel.filterStore
        )
        binding.additionalFilterChips.addGoldCostChips(
            filter.lowerGoldCost,
            filter.upperGoldCost,
            filterStore = viewModel.filterStore
        )
        binding.additionalFilterChips.addRarityChips(
            filter.rarities,
            filterStore = viewModel.filterStore
        )
    }

    private fun bindUi() {
        binding.searchButton.setup(requireActivity(), viewModel.filterStore)
        binding.levelButton.setup(requireActivity(), viewModel.filterStore)
        binding.sortButton.setup(requireActivity(), viewModel.filterStore)
        binding.filterFab.setOnClickListener {
            TreasureFilterBottomSheet.newInstance().show(parentFragmentManager, "tag")
        }
    }


}