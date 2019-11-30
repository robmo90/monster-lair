package de.enduni.monsterlair.hazards

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
import com.google.android.material.bottomsheet.BottomSheetBehavior
import de.enduni.monsterlair.R
import de.enduni.monsterlair.common.openCustomTab
import de.enduni.monsterlair.common.setTextIfNotFocused
import de.enduni.monsterlair.common.toDp
import de.enduni.monsterlair.databinding.FragmentHazardsBinding
import de.enduni.monsterlair.hazards.view.HazardOverviewAction
import de.enduni.monsterlair.hazards.view.HazardOverviewViewState
import de.enduni.monsterlair.hazards.view.HazardType
import de.enduni.monsterlair.hazards.view.HazardViewModel
import de.enduni.monsterlair.hazards.view.adapter.HazardListAdapter
import org.koin.androidx.viewmodel.ext.android.viewModel
import timber.log.Timber

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
        listAdapter = HazardListAdapter(activity!!.layoutInflater, viewModel)

        binding.hazardRecyclerView.adapter = listAdapter
        viewModel.viewState.observe(this, Observer { bindViewToState(it) })
        viewModel.actions.observe(this, Observer { handleAction(it) })
        bindUi()
    }

    private fun bindUi() {
        binding.searchEditText.doAfterTextChanged {
            viewModel.filterByString(it.toString())
        }
        binding.navigateDown.setOnClickListener {
            binding.hazardRecyclerView.layoutManager?.scrollToPosition(listAdapter.itemCount - 1)
        }
        binding.navigateUp.setOnClickListener {
            binding.hazardRecyclerView.layoutManager?.scrollToPosition(0)
        }

        binding.levelSlider.setOnThumbValueChangeListener { _, _, thumbIndex, value ->
            when (thumbIndex) {
                0 -> viewModel.adjustFilterLevelLower(value)
                1 -> viewModel.adjustFilterLevelUpper(value)
            }
        }

        setupBottomSheet()
        ArrayAdapter.createFromResource(
            context!!,
            R.array.hazard_list_filter_choices,
            android.R.layout.simple_spinner_item
        ).also { adapter ->
            // Specify the layout to use when the list of choices appears
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            // Apply the adapter to the spinner
            binding.categorySpinner.adapter = adapter
        }
        binding.categorySpinner.onItemSelectedListener =
            object : AdapterView.OnItemSelectedListener {
                override fun onNothingSelected(parent: AdapterView<*>?) {
                }

                override fun onItemSelected(
                    parent: AdapterView<*>?,
                    view: View?,
                    position: Int,
                    id: Long
                ) {
                    when (position) {
                        0 -> viewModel.adjustType(HazardType.ALL)
                        1 -> viewModel.adjustType(HazardType.SIMPLE)
                        2 -> viewModel.adjustType(HazardType.COMPLEX)
                    }
                }

            }
    }

    private fun setupBottomSheet() {
        val bottomSheetBehavior = BottomSheetBehavior.from(binding.filterBottomSheet)
        bottomSheetBehavior.state = BottomSheetBehavior.STATE_COLLAPSED
        bottomSheetBehavior.peekHeight = 56.toDp(context!!.resources.displayMetrics)
    }

    private fun bindViewToState(state: HazardOverviewViewState) {
        listAdapter.submitList(state.hazards)
        binding.searchEditText.setTextIfNotFocused(state.hazardFilter?.string)
        state.hazardFilter?.let { filter ->
            binding.levelSliderLabel.text = context!!.getString(
                R.string.monster_level_range_values,
                filter.lowerLevel,
                filter.upperLevel
            )
            binding.levelSlider.getThumb(0).value = filter.lowerLevel
            binding.levelSlider.getThumb(1).value = filter.upperLevel
        }
    }

    private fun handleAction(action: HazardOverviewAction?) {
        when (action) {
            is HazardOverviewAction.HazardSelected -> Uri.parse(action.url).openCustomTab(context!!)
            else -> Timber.d("Selected this action")
        }
    }

    override fun onPause() {
        super.onPause()
        viewModel.actions.removeObservers(this)
    }
}