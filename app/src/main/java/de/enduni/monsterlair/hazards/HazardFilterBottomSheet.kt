package de.enduni.monsterlair.hazards

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import de.enduni.monsterlair.R
import de.enduni.monsterlair.common.view.*
import de.enduni.monsterlair.databinding.DialogHazardFilterBinding
import de.enduni.monsterlair.hazards.view.HazardFilterViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.koin.androidx.viewmodel.ext.android.viewModel


@ExperimentalCoroutinesApi
class HazardFilterBottomSheet : BottomSheetDialogFragment() {

    private lateinit var binding: DialogHazardFilterBinding

    private val viewModel: HazardFilterViewModel by viewModel()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.dialog_hazard_filter, container, true)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        binding = DialogHazardFilterBinding.bind(view)

        viewModel.traits.observe(
            viewLifecycleOwner,
            Observer { binding.traitsSelect.setupTraitsSelect(it, viewModel.filterStore) }
        )
        viewModel.filter.observe(viewLifecycleOwner, Observer { filter ->
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
            binding.rarityChips.buildRaritySelection(
                filter.rarities,
                filterStore = viewModel.filterStore
            )
        })

        binding.traitsSelect.adjustBottomSheetPadding(binding.root)

    }


    companion object {

        fun newInstance() = HazardFilterBottomSheet()

    }

}