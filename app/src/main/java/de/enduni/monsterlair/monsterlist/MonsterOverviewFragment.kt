package de.enduni.monsterlair.monsterlist

import android.os.Bundle
import android.text.Editable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.widget.doAfterTextChanged
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import de.enduni.monsterlair.R
import de.enduni.monsterlair.databinding.FragmentMonsterOverviewBinding
import de.enduni.monsterlair.monsterlist.view.MonsterOverviewViewState
import de.enduni.monsterlair.monsterlist.view.MonsterViewModel
import de.enduni.monsterlair.monsterlist.view.adapter.MonsterListAdapter
import org.koin.androidx.viewmodel.ext.android.viewModel

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
        listAdapter =
            MonsterListAdapter(activity!!.layoutInflater)
        binding.monsterRecyclerView.adapter = listAdapter
        viewModel.viewState.observe(this, Observer { state ->
            bindViewToState(state)
        })

        binding.searchEditText.doAfterTextChanged {
            viewModel.filterByString(it.toString())
        }

        binding.levelSlider.setOnThumbValueChangeListener { _, _, thumbIndex, value ->
            when (thumbIndex) {
                0 -> viewModel.adjustFilterLevelLower(value)
                1 -> viewModel.adjustFilterLevelUpper(value)
            }
        }
    }

    private fun bindViewToState(state: MonsterOverviewViewState) {
        listAdapter.submitList(state.monsters)
        if (!binding.searchEditText.isFocused) {
            if (!state.filter?.string.isNullOrEmpty()) {
                binding.searchEditText.text =
                    Editable.Factory.getInstance().newEditable(state.filter?.string)
            }
        }
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


}