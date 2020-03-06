package de.enduni.monsterlair.monsters

import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.widget.doAfterTextChanged
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import com.google.android.material.bottomsheet.BottomSheetBehavior
import de.enduni.monsterlair.R
import de.enduni.monsterlair.common.openCustomTab
import de.enduni.monsterlair.common.setTextIfNotFocused
import de.enduni.monsterlair.common.view.buildMonsterTypeFilter
import de.enduni.monsterlair.common.view.buildSortByChips
import de.enduni.monsterlair.databinding.FragmentMonsterBinding
import de.enduni.monsterlair.monsters.view.MonsterOverviewAction
import de.enduni.monsterlair.monsters.view.MonsterOverviewViewState
import de.enduni.monsterlair.monsters.view.MonsterViewModel
import de.enduni.monsterlair.monsters.view.adapter.MonsterListAdapter
import org.koin.androidx.viewmodel.ext.android.viewModel
import timber.log.Timber


class MonsterFragment : Fragment() {

    private lateinit var binding: FragmentMonsterBinding

    private lateinit var listAdapter: MonsterListAdapter

    private val viewModel: MonsterViewModel by viewModel()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return layoutInflater.inflate(R.layout.fragment_monster, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        binding = FragmentMonsterBinding.bind(view)
        listAdapter = MonsterListAdapter(requireActivity().layoutInflater, viewModel)

        binding.monsterRecyclerView.adapter = listAdapter
        viewModel.viewState.observe(
            viewLifecycleOwner,
            Observer { state -> bindViewToState(state) })
        bindUi()
        viewModel.start()
    }

    override fun onResume() {
        super.onResume()
        viewModel.actions.observe(this, Observer { handleAction(it) })
    }

    private fun bindUi() {
        binding.searchEditText.doAfterTextChanged {
            viewModel.filterByString(it.toString())
        }
        binding.navigateDown.setOnClickListener {
            binding.monsterRecyclerView.layoutManager?.scrollToPosition(listAdapter.itemCount - 1)
        }
        binding.navigateUp.setOnClickListener {
            binding.monsterRecyclerView.layoutManager?.scrollToPosition(0)
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

    private fun bindViewToState(state: MonsterOverviewViewState) {
        listAdapter.submitList(state.monsters)
        binding.searchEditText.setTextIfNotFocused(state.filter?.string)

        state.filter?.let { filter ->
            binding.levelSliderLabel.text = requireContext().getString(
                R.string.monster_level_range_values,
                filter.lowerLevel,
                filter.upperLevel
            )
            binding.levelSlider.getThumb(0).value = filter.lowerLevel
            binding.levelSlider.getThumb(1).value = filter.upperLevel
            binding.monsterTypeChips.buildMonsterTypeFilter(
                filter.monsterTypes,
                { viewModel.addMonsterTypeFilter(it) },
                { viewModel.removeMonsterTypeFilter(it) }

            )
            binding.sortByChips.buildSortByChips(
                filter.sortBy
            ) { viewModel.adjustSortBy(it) }
        }

    }

    private fun handleAction(action: MonsterOverviewAction?) {
        when (action) {
            is MonsterOverviewAction.MonsterSelected -> navigateToUrl(action.url)
            else -> Timber.d("Processed $action")
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