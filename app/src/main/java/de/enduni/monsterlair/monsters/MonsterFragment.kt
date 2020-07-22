package de.enduni.monsterlair.monsters

import android.net.Uri
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import de.enduni.monsterlair.R
import de.enduni.monsterlair.common.openCustomTab
import de.enduni.monsterlair.common.view.*
import de.enduni.monsterlair.databinding.FragmentMonsterBinding
import de.enduni.monsterlair.monsters.domain.MonsterFilter
import de.enduni.monsterlair.monsters.view.MonsterOverviewAction
import de.enduni.monsterlair.monsters.view.MonsterViewModel
import de.enduni.monsterlair.monsters.view.adapter.MonsterListAdapter
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.koin.androidx.viewmodel.ext.android.viewModel
import timber.log.Timber


@ExperimentalCoroutinesApi
class MonsterFragment : Fragment(R.layout.fragment_monster) {

    private lateinit var binding: FragmentMonsterBinding

    private lateinit var listAdapter: MonsterListAdapter

    private val viewModel: MonsterViewModel by viewModel()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        binding = FragmentMonsterBinding.bind(view)
        listAdapter = MonsterListAdapter(requireActivity().layoutInflater, viewModel)
        binding.monsterRecyclerView.adapter = listAdapter

        viewModel.monsters.observe(viewLifecycleOwner, Observer { listAdapter.submitList(it) })
        viewModel.filter.observe(viewLifecycleOwner, Observer { update(it) })
        bindUi()
    }

    fun update(filter: MonsterFilter) {
        binding.levelButton.update(filter.lowerLevel, filter.upperLevel)
        binding.sortButton.update(filter.sortBy)
        binding.searchButton.update(filter.searchTerm)
        binding.additionalFilterChips.removeAllViews()
        binding.additionalFilterChips.addMonsterTypeChips(
            filter.types, filterStore = viewModel.filterStore
        )
        binding.additionalFilterChips.addTraitChips(
            filter.traits,
            filterStore = viewModel.filterStore
        )
        binding.additionalFilterChips.addAlignmentChips(
            filter.alignments, filterStore = viewModel.filterStore
        )
        binding.additionalFilterChips.addSizeChips(
            filter.sizes, filterStore = viewModel.filterStore
        )
        binding.additionalFilterChips.addRarityChips(
            filter.rarities,
            filterStore = viewModel.filterStore
        )
    }

    override fun onResume() {
        super.onResume()
        viewModel.actions.observe(this, Observer { handleAction(it) })
    }

    private fun bindUi() {
        binding.searchButton.setup(requireActivity(), viewModel.filterStore)
        binding.levelButton.setup(requireActivity(), viewModel.filterStore)
        binding.sortButton.setup(requireActivity(), viewModel.filterStore)
        binding.filterFab.setOnClickListener {
            MonsterFilterBottomSheet.newInstance().show(parentFragmentManager, "tag")
        }
    }

    private fun handleAction(action: MonsterOverviewAction?) {
        when (action) {
            is MonsterOverviewAction.OnMonsterLinkClicked -> {
                navigateToUrl(action.url)
            }
            is MonsterOverviewAction.OnCustomMonsterClicked -> {
                showCustomMonsterHint()
            }
            is MonsterOverviewAction.OnCustomMonsterPressed -> {
                EditMonsterDialog(
                    requireActivity(),
                    action.id,
                    action.monsterName,
                    viewModel
                ).show()
            }
            is MonsterOverviewAction.OnEditCustomMonsterClicked -> {
                CreateMonsterDialog(requireActivity(), viewModel, action.monster).show()
            }
            else -> Timber.d("Processed $action")
        }
    }

    private fun showCustomMonsterHint() {
        Toast.makeText(requireContext(), R.string.custom_monster_hint, Toast.LENGTH_SHORT).show()
    }


    private fun navigateToUrl(url: String) {
        Uri.parse(url).openCustomTab(requireContext())
    }

    override fun onPause() {
        super.onPause()
        viewModel.actions.removeObservers(this)
    }

}