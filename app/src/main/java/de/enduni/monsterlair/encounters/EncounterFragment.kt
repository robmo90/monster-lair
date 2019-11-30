package de.enduni.monsterlair.encounters

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import androidx.core.widget.doAfterTextChanged
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import com.google.android.material.bottomsheet.BottomSheetDialog
import de.enduni.monsterlair.R
import de.enduni.monsterlair.common.setTextIfNotFocused
import de.enduni.monsterlair.databinding.BottomsheetEncounterBinding
import de.enduni.monsterlair.databinding.FragmentEncountersBinding
import de.enduni.monsterlair.encounters.domain.model.EncounterDifficulty
import de.enduni.monsterlair.encounters.view.EncounterAction
import de.enduni.monsterlair.encounters.view.EncounterState
import de.enduni.monsterlair.encounters.view.EncounterViewModel
import de.enduni.monsterlair.encounters.view.adapter.EncounterListAdapter
import org.koin.androidx.viewmodel.ext.android.viewModel

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
        setupDifficultySelect()
        viewModel.fetchEncounters()
    }

    private fun handleAction(action: EncounterAction) {
        when (action) {
            is EncounterAction.EncounterSelectedAction -> {
                val directions =
                    EncounterFragmentDirections.openEncounterCreatorAction(
                        encounterLevel = action.encounterLevel,
                        numberOfPlayers = action.numberOfPlayers,
                        encounterDifficulty = action.difficulty,
                        encounterId = action.encounterId
                    )
                findNavController().navigate(directions)
            }
            is EncounterAction.ExportEncounterToPdfAction -> {
                val directions =
                    EncounterFragmentDirections.exportEncounterAction(
                        action.encounterName,
                        action.template
                    )
                findNavController().navigate(directions)
            }
            is EncounterAction.EncounterDetailsOpenedAction -> showBottomSheet(
                action.encounterName,
                action.id
            )
        }
    }

    private fun handleViewState(state: EncounterState) {
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
                EncounterFragmentDirections.openEncounterCreatorAction(
                    encounterLevel = state.levelOfPlayers!!,
                    numberOfPlayers = state.numberOfPlayers!!,
                    encounterDifficulty = state.difficulty
                )
            findNavController().navigate(directions)
        }
        if (state.encounters.isEmpty()) {
            binding.encounterRecyclerView.visibility = View.GONE
            binding.encounterLabel.visibility = View.GONE
        } else {
            binding.encounterRecyclerView.visibility = View.VISIBLE
            binding.encounterLabel.visibility = View.VISIBLE
            adapter.submitList(state.encounters)
        }
    }

    private fun showBottomSheet(name: String, id: Long) {
        val binding = BottomsheetEncounterBinding.inflate(activity!!.layoutInflater, null, false)
        val bottomSheetDialog = BottomSheetDialog(context!!)
        binding.encounterName.text = name
        binding.printEncounter.setOnClickListener {
            viewModel.onEncounterExport(id)
            bottomSheetDialog.dismiss()
        }
        binding.deleteEncounter.setOnClickListener {
            viewModel.onEncounterDeleted(id)
            bottomSheetDialog.dismiss()
        }
        bottomSheetDialog.setContentView(binding.root)
        bottomSheetDialog.show()
    }

    private fun setupTextListeners() {
        binding.characterLevelEditText.doAfterTextChanged { editable ->
            viewModel.setLevel(editable.toString())
        }
        binding.characterNumberEditText.doAfterTextChanged { editable ->
            viewModel.setNumber(editable.toString())
        }
    }

    private fun setupDifficultySelect() {
        ArrayAdapter.createFromResource(
            context!!,
            R.array.difficulty_choices,
            android.R.layout.simple_spinner_item
        ).also { adapter ->
            binding.difficultySelect.setAdapter(adapter)
        }

        binding.difficultySelect.doAfterTextChanged { choice ->
            context?.resources?.getStringArray(R.array.difficulty_choices)?.let { choices ->
                when (choices.indexOf(choice.toString())) {
                    0 -> viewModel.setDifficulty(EncounterDifficulty.TRIVIAL)
                    1 -> viewModel.setDifficulty(EncounterDifficulty.LOW)
                    2 -> viewModel.setDifficulty(EncounterDifficulty.MODERATE)
                    3 -> viewModel.setDifficulty(EncounterDifficulty.SEVERE)
                    4 -> viewModel.setDifficulty(EncounterDifficulty.EXTREME)
                    else -> return@doAfterTextChanged
                }
            }
        }
    }

    override fun onPause() {
        super.onPause()
        viewModel.actions.removeObservers(this)
    }
}