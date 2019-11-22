package de.enduni.monsterlair.overview

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
        listAdapter = MonsterListAdapter(activity!!.layoutInflater)
        binding.monsterRecyclerView.adapter = listAdapter
        viewModel.viewState.observe(this, Observer { state ->
            bindViewToState(state)
        })

        binding.searchEditText.doAfterTextChanged {
            viewModel.filterByString(it.toString())
        }

        binding.levelSlider.setOnThumbValueChangeListener { multiSlider, _, _, _ ->
            val lowerLevel = multiSlider.getThumb(0).value
            val higherLevel = multiSlider.getThumb(1).value
            viewModel.filterByLevel(lowerLevel, higherLevel)
        }
    }

    private fun bindViewToState(state: MonsterOverviewViewState) {
        listAdapter.submitList(state.monsters)
        if (!binding.searchEditText.isFocused && state.filter?.string != null) {
            binding.searchEditText.text =
                Editable.Factory.getInstance().newEditable(state.filter.string)
        }
        state.filter?.let { filter ->
            binding.levelSliderLabel.text = context!!.getString(
                R.string.monster_level_range_values,
                filter.lowerLevel,
                filter.higherLevel
            )
        }
    }
}