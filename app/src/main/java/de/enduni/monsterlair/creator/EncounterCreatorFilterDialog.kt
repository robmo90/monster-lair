package de.enduni.monsterlair.creator

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import androidx.lifecycle.Observer
import de.enduni.monsterlair.R
import de.enduni.monsterlair.common.view.*
import de.enduni.monsterlair.creator.view.EncounterCreatorFilterViewModel
import de.enduni.monsterlair.databinding.DialogEncounterCreatorFilterBinding
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.koin.androidx.viewmodel.ext.android.viewModel


@ExperimentalCoroutinesApi
class EncounterCreatorFilterDialog : DialogFragment() {

    private lateinit var binding: DialogEncounterCreatorFilterBinding

    private val viewModel: EncounterCreatorFilterViewModel by viewModel()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setStyle(STYLE_NO_FRAME, R.style.AppTheme);
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.dialog_encounter_creator_filter, container, true)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        binding = DialogEncounterCreatorFilterBinding.bind(view)

        binding.toolbar.setNavigationIcon(R.drawable.ic_close)
        binding.toolbar.setNavigationOnClickListener { dismiss() }
        binding.closeButton.setOnClickListener { dismiss() }
        viewModel.traits.observe(
            viewLifecycleOwner,
            Observer { binding.traitsSelect.setupTraitsSelect(it, viewModel.filterStore) }
        )
        viewModel.filter.observe(viewLifecycleOwner, Observer { filter ->
            binding.withinBudgetCheckbox.isChecked = filter.withinBudget
            binding.monsterTypeChips.addMonsterTypeChips(
                filter.types,
                clear = true,
                filterStore = viewModel.filterStore
            )
            binding.dangerTypeChips.buildDangerTypeSelection(
                filter.dangerTypes,
                viewModel.filterStore
            )
            binding.complexityChips.buildComplexitySelection(
                filter.complexities,
                clear = true,
                filterStore = viewModel.filterStore
            )
            binding.traitsChips.addTraitChips(
                filter.traits,
                clear = true,
                filterStore = viewModel.filterStore
            )
            binding.alignmentChips.addAlignmentChips(
                filter.alignments,
                clear = true,
                filterStore = viewModel.filterStore
            )
            binding.sizeChips.addSizeChips(
                filter.sizes,
                clear = true,
                filterStore = viewModel.filterStore
            )
            binding.rarityChips.buildRaritySelection(
                filter.rarities,
                filterStore = viewModel.filterStore
            )
        })

        binding.withinBudgetCheckbox.setOnCheckedChangeListener { _, isChecked ->
            viewModel.filterStore.setFilterWithinBudget(isChecked)
        }
        binding.monsterTypeSelect.setupMonsterTypeSelect(viewModel.filterStore)
        binding.alignmentSelect.setupAlignmentSelect(viewModel.filterStore)
        binding.sizeSelect.setupSizeSelect(viewModel.filterStore)
    }


    companion object {

        fun newInstance() =
            EncounterCreatorFilterDialog()

    }

}