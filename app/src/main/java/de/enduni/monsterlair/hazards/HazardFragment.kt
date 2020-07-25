package de.enduni.monsterlair.hazards

import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import de.enduni.monsterlair.R
import de.enduni.monsterlair.common.openCustomTab
import de.enduni.monsterlair.common.view.addComplexityChips
import de.enduni.monsterlair.common.view.addRarityChips
import de.enduni.monsterlair.common.view.addTraitChips
import de.enduni.monsterlair.databinding.FragmentHazardsBinding
import de.enduni.monsterlair.hazards.domain.HazardFilter
import de.enduni.monsterlair.hazards.view.HazardOverviewAction
import de.enduni.monsterlair.hazards.view.HazardViewModel
import de.enduni.monsterlair.hazards.view.adapter.HazardListAdapter
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.koin.androidx.viewmodel.ext.android.viewModel
import timber.log.Timber

@ExperimentalCoroutinesApi
class HazardFragment : Fragment() {

    private val viewModel: HazardViewModel by viewModel()

    private lateinit var binding: FragmentHazardsBinding

    private lateinit var listAdapter: HazardListAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return layoutInflater.inflate(R.layout.fragment_hazards, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentHazardsBinding.bind(view)
        listAdapter = HazardListAdapter(requireActivity().layoutInflater, viewModel)

        binding.hazardRecyclerView.adapter = listAdapter
        viewModel.hazards.observe(viewLifecycleOwner, Observer {
            binding.emptyLayout.visibility = if (it.isEmpty()) View.VISIBLE else View.GONE
            binding.hazardRecyclerView.visibility = if (it.isEmpty()) View.GONE else View.VISIBLE
            listAdapter.submitList(it)
        })
        viewModel.filter.observe(viewLifecycleOwner, Observer { updateUi(it) })
        bindUi()
    }

    override fun onResume() {
        super.onResume()
        viewModel.actions.observe(this, Observer { handleAction(it) })
    }


    private fun updateUi(filter: HazardFilter) {
        binding.levelButton.update(filter.lowerLevel, filter.upperLevel)
        binding.sortButton.update(filter.sortBy)
        binding.searchButton.update(filter.searchTerm)
        binding.additionalFilterChips.removeAllViews()
        binding.additionalFilterChips.addComplexityChips(
            filter.complexities,
            filterStore = viewModel.filterStore
        )
        binding.additionalFilterChips.addTraitChips(
            filter.traits,
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
            HazardFilterBottomSheet.newInstance().show(parentFragmentManager, "tag")
        }
    }


    private fun handleAction(action: HazardOverviewAction?) {
        when (action) {
            is HazardOverviewAction.HazardSelected -> Uri.parse(action.url)
                .openCustomTab(requireContext())
            else -> Timber.d("Selected this action")
        }
    }

    override fun onPause() {
        super.onPause()
        viewModel.actions.removeObservers(this)
    }
}