package de.enduni.monsterlair.encounters.creator

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import androidx.core.widget.doAfterTextChanged
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.SimpleItemAnimator
import de.enduni.monsterlair.R
import de.enduni.monsterlair.common.setTextIfNotFocused
import de.enduni.monsterlair.databinding.FragmentEncounterCreatorBinding
import de.enduni.monsterlair.encounters.creator.view.EncounterCreatorAction
import de.enduni.monsterlair.encounters.creator.view.EncounterCreatorDisplayState
import de.enduni.monsterlair.encounters.creator.view.EncounterCreatorViewModel
import de.enduni.monsterlair.encounters.creator.view.SaveDialog
import de.enduni.monsterlair.encounters.creator.view.adapter.EncounterCreatorListAdapter
import de.enduni.monsterlair.monsters.view.SortBy
import org.koin.androidx.viewmodel.ext.android.viewModel
import timber.log.Timber


class EncounterCreatorFragment : Fragment() {

    private lateinit var binding: FragmentEncounterCreatorBinding

    private lateinit var listAdapter: EncounterCreatorListAdapter

    private val viewModel: EncounterCreatorViewModel by viewModel()

    private val args: EncounterCreatorFragmentArgs by navArgs()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return layoutInflater.inflate(R.layout.fragment_encounter_creator, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        binding = FragmentEncounterCreatorBinding.bind(view)
        listAdapter = EncounterCreatorListAdapter(
            activity!!.layoutInflater, viewModel,
            viewModel, viewModel
        )

        binding.encounterRecyclerView.adapter = listAdapter
        val animator = binding.encounterRecyclerView.itemAnimator
        if (animator is SimpleItemAnimator) {
            Timber.d("Disabling")
            animator.supportsChangeAnimations = false
        }
        viewModel.viewState.observe(this, Observer { state -> bindViewToState(state) })
        viewModel.actions.observe(this, Observer { action -> handleAction(action) })
        bindUi()

        viewModel.start(
            numberOfPlayers = args.numberOfPlayers,
            levelOfEncounter = args.encounterLevel,
            targetDifficulty = args.encounterDifficulty,
            encounterId = args.encounterId
        )
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

    private fun bindViewToState(state: EncounterCreatorDisplayState) {
        listAdapter.submitList(state.list)
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

    private fun handleAction(action: EncounterCreatorAction?) {
        when (action) {
            is EncounterCreatorAction.SaveClicked -> {
                SaveDialog.show(activity!!, action.name) { name ->
                    viewModel.saveEncounter(name)
                }
            }
            is EncounterCreatorAction.EncounterSaved -> {
                findNavController().navigateUp()
            }
            else -> return
        }
    }

}