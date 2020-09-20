package de.enduni.monsterlair.monsters

import android.net.Uri
import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import com.google.android.material.bottomsheet.BottomSheetDialog
import de.enduni.monsterlair.R
import de.enduni.monsterlair.common.openCustomTab
import de.enduni.monsterlair.common.view.*
import de.enduni.monsterlair.databinding.BottomsheetMonstersBinding
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

        viewModel.monsters.observe(viewLifecycleOwner, Observer {
            binding.emptyLayout.visibility = if (it.isEmpty()) View.VISIBLE else View.GONE
            binding.monsterRecyclerView.visibility = if (it.isEmpty()) View.GONE else View.VISIBLE
            listAdapter.submitList(it)
        })
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
        Timber.d("Resuming")
        viewModel.actions.observe(this, Observer { handleAction(it) })
    }

    private fun bindUi() {
        binding.searchButton.setup(requireActivity(), viewModel.filterStore)
        binding.levelButton.setup(
            requireActivity(),
            viewModel.filterStore,
            lowerLevel = -1,
            upperLevel = 25
        )
        binding.sortButton.setup(requireActivity(), viewModel.filterStore)
        binding.moreOptions.setOnClickListener { showBottomSheet() }
        binding.filterFab.setOnClickListener {
            MonsterFilterBottomSheet.newInstance().show(parentFragmentManager, "tag")
        }

    }

    private fun handleAction(action: MonsterOverviewAction?) {
        when (action) {
            is MonsterOverviewAction.OnMonsterLinkClicked -> {
                navigateToUrl(action.url)
            }
            is MonsterOverviewAction.OnEditCustomMonsterClicked -> {
                CreateMonsterDialog.newInstance(action.monster.id)
                    .show(parentFragmentManager, "tag")
            }
            is MonsterOverviewAction.OnDeleteCustomMonsterClicked -> {
                DeleteDialog(requireActivity(), action.monster.id, action.monster.name) {
                    viewModel.onDeleteConfirmed(monsterId = action.monster.id)
                }.show()
            }
            else -> Timber.d("Processed $action")
        }
    }

    private fun showBottomSheet() {
        val binding =
            BottomsheetMonstersBinding.inflate(requireActivity().layoutInflater, null, false)
        binding.addMonster.setOnClickListener {
            CreateMonsterDialog.newInstance(null).show(parentFragmentManager, "tag")
        }
        BottomSheetDialog(requireContext()).apply {
            setContentView(binding.root)
            show()
        }
    }


    private fun navigateToUrl(url: String) {
        Uri.parse(url).openCustomTab(requireContext())
    }

    override fun onPause() {
        super.onPause()
        viewModel.actions.removeObservers(this)
    }

}