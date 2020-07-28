package de.enduni.monsterlair.treasure

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import com.google.android.material.bottomsheet.BottomSheetDialog
import de.enduni.monsterlair.R
import de.enduni.monsterlair.common.view.*
import de.enduni.monsterlair.databinding.BottomsheetTreasureBinding
import de.enduni.monsterlair.databinding.FragmentTreasureBinding
import de.enduni.monsterlair.encounters.export.EncounterExportActivity
import de.enduni.monsterlair.treasure.domain.TreasureFilter
import de.enduni.monsterlair.treasure.view.TreasureAction
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
        viewModel.treasures.observe(viewLifecycleOwner, Observer {
            binding.emptyLayout.visibility = if (it.isEmpty()) View.VISIBLE else View.GONE
            binding.treasureRecyclerView.visibility = if (it.isEmpty()) View.GONE else View.VISIBLE
            listAdapter.submitList(it)
        })
        viewModel.filter.observe(viewLifecycleOwner, Observer { updateUi(it) })

        bindUi()
    }

    override fun onResume() {
        super.onResume()
        viewModel.actions.observe(viewLifecycleOwner, Observer { action ->
            when (action) {
                is TreasureAction.GeneratedTreasure -> {
                    EncounterExportActivity.intent(
                        requireContext(),
                        getString(R.string.random_treasure),
                        action.html
                    ).apply { startActivity(this) }
                }
                is TreasureAction.NotEnoughTreasure -> {
                    Toast.makeText(
                        requireContext(),
                        R.string.create_random_treasure_error,
                        Toast.LENGTH_LONG
                    ).show()
                }
            }
        })
    }

    override fun onPause() {
        super.onPause()
        viewModel.actions.removeObservers(viewLifecycleOwner)
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
        binding.levelButton.setup(
            requireActivity(),
            viewModel.filterStore,
            lowerLevel = 0,
            upperLevel = 28
        )
        binding.sortButton.setup(requireActivity(), viewModel.filterStore)
        binding.filterFab.setOnClickListener {
            TreasureFilterBottomSheet.newInstance().show(parentFragmentManager, "tag")
        }
        binding.moreOptions.setOnClickListener {
            showBottomSheet()
        }
    }

    private fun showBottomSheet() {
        val binding =
            BottomsheetTreasureBinding.inflate(requireActivity().layoutInflater, null, false)
        binding.randomTreasure.setOnClickListener {
            CreateRandomTreasureDialog(requireActivity(), viewModel).show()
        }
        BottomSheetDialog(requireContext()).apply {
            setContentView(binding.root)
            show()
        }
    }

}