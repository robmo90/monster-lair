package de.enduni.monsterlair.monsters

import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import androidx.core.widget.doAfterTextChanged
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import de.enduni.monsterlair.R
import de.enduni.monsterlair.common.openCustomTab
import de.enduni.monsterlair.common.setTextIfNotFocused
import de.enduni.monsterlair.databinding.FragmentMonsterOverviewBinding
import de.enduni.monsterlair.monsters.view.MonsterOverviewAction
import de.enduni.monsterlair.monsters.view.MonsterOverviewViewState
import de.enduni.monsterlair.monsters.view.MonsterViewModel
import de.enduni.monsterlair.monsters.view.SortBy
import de.enduni.monsterlair.monsters.view.adapter.MonsterListAdapter
import org.koin.androidx.viewmodel.ext.android.viewModel
import timber.log.Timber


class MonsterOverviewFragment : Fragment() {

    private lateinit var binding: FragmentMonsterOverviewBinding

    private lateinit var listAdapter: MonsterListAdapter

    private val viewModel: MonsterViewModel by viewModel()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return layoutInflater.inflate(R.layout.fragment_monster_overview, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        binding = FragmentMonsterOverviewBinding.bind(view)
        listAdapter = MonsterListAdapter(activity!!.layoutInflater, viewModel)

        binding.monsterRecyclerView.adapter = listAdapter
        viewModel.viewState.observe(this, Observer { state -> bindViewToState(state) })
        viewModel.actions.observe(this, Observer { action -> handleAction(action) })
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

        ArrayAdapter.createFromResource(
            context!!,
            R.array.monster_list_sort_choices,
            android.R.layout.simple_spinner_item
        ).also { adapter ->
            // Specify the layout to use when the list of choices appears
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            // Apply the adapter to the spinner
            binding.typeSpinner.adapter = adapter
        }
        binding.typeSpinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onNothingSelected(parent: AdapterView<*>?) {
            }

            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {
                when (position) {
                    0 -> viewModel.adjustSortBy(SortBy.NAME)
                    1 -> viewModel.adjustSortBy(SortBy.LEVEL)
                    2 -> viewModel.adjustSortBy(SortBy.TYPE)
                }
            }

        }
    }

    private fun bindViewToState(state: MonsterOverviewViewState) {
        listAdapter.submitList(state.monsters)
        binding.searchEditText.setTextIfNotFocused(state.filter?.string)
        state.filter?.let { filter ->
            binding.levelSliderLabel.text = context!!.getString(
                R.string.monster_level_range_values,
                filter.lowerLevel,
                filter.upperLevel
            )
            binding.levelSlider.getThumb(0).value = filter.lowerLevel
            binding.levelSlider.getThumb(1).value = filter.upperLevel
        }
    }

    private fun handleAction(action: MonsterOverviewAction?) {
        when (action) {
            is MonsterOverviewAction.MonsterSelected -> navigateToUrl(action.url)
            else -> Timber.d("Processed $action")
        }
    }


    private fun navigateToUrl(url: String) {
        Uri.parse(url).openCustomTab(context!!)
    }

}