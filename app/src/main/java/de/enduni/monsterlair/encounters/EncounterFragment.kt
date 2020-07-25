package de.enduni.monsterlair.encounters

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import com.google.android.material.bottomsheet.BottomSheetDialog
import de.enduni.monsterlair.R
import de.enduni.monsterlair.common.view.EncounterSettingDialog
import de.enduni.monsterlair.creator.EncounterCreatorActivity
import de.enduni.monsterlair.databinding.BottomsheetEncounterBinding
import de.enduni.monsterlair.databinding.FragmentEncountersBinding
import de.enduni.monsterlair.encounters.export.EncounterExportActivity
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

        viewModel.viewState.observe(viewLifecycleOwner, Observer { handleViewState(it) })
        adapter = EncounterListAdapter(requireActivity().layoutInflater, viewModel)
        binding.encounterRecyclerView.adapter = adapter
        viewModel.fetchEncounters()
    }

    override fun onResume() {
        super.onResume()
        viewModel.actions.observe(this, Observer { handleAction(it) })
    }

    private fun handleAction(action: EncounterAction) {
        when (action) {
            is EncounterAction.EncounterSelectedAction -> {
                val intent = EncounterCreatorActivity.intent(
                    context = requireContext(),
                    encounterName = action.encounterName,
                    encounterLevel = action.encounterLevel,
                    numberOfPlayers = action.numberOfPlayers,
                    difficulty = action.difficulty,
                    useProficiencyWithoutLevel = action.useProficiencyWithoutLevel,
                    notes = action.notes,
                    encounterId = action.encounterId

                )
                requireActivity().startActivity(intent)
            }
            is EncounterAction.ExportEncounterToPdfAction -> {
                EncounterExportActivity.intent(
                    requireContext(),
                    action.encounterName,
                    action.template
                ).apply {
                    startActivity(this)
                }
            }
            is EncounterAction.EncounterDetailsOpenedAction -> showBottomSheet(
                action.encounterName,
                action.id
            )
        }
    }

    private fun handleViewState(state: EncounterState) {
        if (state.encounters?.isEmpty() == true) {
            binding.encounterRecyclerView.visibility = View.GONE
            binding.emptyEncounterLayout.visibility = View.VISIBLE
            binding.addFab.visibility = View.GONE

            binding.startButton.setOnClickListener { showEncounterSettingDialog() }
        } else {
            binding.encounterRecyclerView.visibility = View.VISIBLE
            binding.emptyEncounterLayout.visibility = View.GONE
            binding.addFab.visibility = View.VISIBLE
            adapter.submitList(state.encounters)

            binding.addFab.setOnClickListener { showEncounterSettingDialog() }
        }
    }

    private fun showEncounterSettingDialog() {
        EncounterSettingDialog(
            EncounterSettingDialog.Purpose.CREATE,
            activity = requireActivity()
        )
            .show { result ->
                EncounterCreatorActivity.intent(
                    context = requireActivity(),
                    encounterName = result.encounterName,
                    numberOfPlayers = result.numberOfPlayers,
                    encounterLevel = result.encounterLevel,
                    difficulty = result.encounterDifficulty,
                    useProficiencyWithoutLevel = result.useProficiencyWithoutLevel,
                    notes = result.notes
                ).let { startActivity(it) }
            }
    }

    private fun showBottomSheet(name: String, id: Long) {
        val binding =
            BottomsheetEncounterBinding.inflate(requireActivity().layoutInflater, null, false)
        val bottomSheetDialog = BottomSheetDialog(requireContext())
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

    override fun onPause() {
        super.onPause()
        viewModel.actions.removeObservers(this)
    }
}