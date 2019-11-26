package de.enduni.monsterlair.encounters

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
import de.enduni.monsterlair.R
import de.enduni.monsterlair.common.setTextIfNotFocused
import de.enduni.monsterlair.databinding.FragmentEncountersBinding
import de.enduni.monsterlair.encounters.domain.EncounterDifficulty
import de.enduni.monsterlair.encounters.view.EncounterSelectedAction
import de.enduni.monsterlair.encounters.view.EncounterState
import de.enduni.monsterlair.encounters.view.EncounterViewModel
import de.enduni.monsterlair.encounters.view.adapter.EncounterListAdapter
import org.koin.androidx.viewmodel.ext.android.viewModel
import timber.log.Timber

class EncounterFragment : Fragment() {

    private val viewModel: EncounterViewModel by viewModel()

    private lateinit var binding: FragmentEncountersBinding

    private lateinit var adapter: EncounterListAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return layoutInflater.inflate(R.layout.fragment_encounters, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentEncountersBinding.bind(view)

        viewModel.viewState.observe(this, Observer { handleViewState(it) })
        viewModel.actions.observe(this, Observer { handleAction(it) })
        adapter = EncounterListAdapter(activity!!.layoutInflater, viewModel)
        binding.encounterRecyclerView.adapter = adapter
        setupTextListeners()
        setupSpinner()
        viewModel.start()
    }

    private fun handleAction(action: EncounterSelectedAction?) {
        action?.let {
            val directions =
                EncounterFragmentDirections.actionEncountersFragmentToEncounterCreatorFragment(
                    encounterLevel = it.encounterLevel,
                    numberOfPlayers = it.numberOfPlayers,
                    encounterDifficulty = it.difficulty,
                    encounterId = it.encounterId
                )
            findNavController().navigate(directions)
        }
    }

    private fun handleViewState(state: EncounterState) {
        Timber.d("Rendering state $state")
        binding.characterLevelEditText.setTextIfNotFocused(state.levelOfPlayers)
        if (state.levelValid.not()) {
            binding.characterLevelEditText.error = getString(R.string.enter_valid_level)
        }
        binding.characterNumberEditText.setTextIfNotFocused(state.numberOfPlayers)
        if (state.numberValid.not()) {
            binding.characterNumberEditText.error = getString(R.string.enter_valid_number)
        }
        binding.startButton.isEnabled = state.isStartAllowed()
        binding.startButton.setOnClickListener {
            val directions =
                EncounterFragmentDirections.actionEncountersFragmentToEncounterCreatorFragment(
                    encounterLevel = state.levelOfPlayers!!,
                    numberOfPlayers = state.numberOfPlayers!!,
                    encounterDifficulty = state.difficulty
                )
            findNavController().navigate(directions)
        }
        adapter.submitList(state.encounters)
    }

    private fun setupTextListeners() {
        binding.characterLevelEditText.doAfterTextChanged { editable ->
            viewModel.setLevel(editable.toString())
        }
        binding.characterNumberEditText.doAfterTextChanged { editable ->
            viewModel.setNumber(editable.toString())
        }
    }

    private fun setupSpinner() {
        ArrayAdapter.createFromResource(
            context!!,
            R.array.difficulty_choices,
            android.R.layout.simple_spinner_item
        ).also { adapter ->
            // Specify the layout to use when the list of choices appears
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            // Apply the adapter to the spinner
            binding.difficultySpinner.adapter = adapter
        }
        binding.difficultySpinner.onItemSelectedListener =
            object : AdapterView.OnItemSelectedListener {
                override fun onNothingSelected(parent: AdapterView<*>?) {
                    TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
                }

                override fun onItemSelected(
                    parent: AdapterView<*>?,
                    view: View?,
                    position: Int,
                    id: Long
                ) {
                    when (position) {
                        0 -> viewModel.setDifficulty(EncounterDifficulty.TRIVIAL)
                        1 -> viewModel.setDifficulty(EncounterDifficulty.LOW)
                        2 -> viewModel.setDifficulty(EncounterDifficulty.MODERATE)
                        3 -> viewModel.setDifficulty(EncounterDifficulty.SEVERE)
                        4 -> viewModel.setDifficulty(EncounterDifficulty.EXTREME)
                    }
                }

            }
    }

    override fun onPause() {
        super.onPause()
        viewModel.actions.removeObservers(this)
    }
}