package de.enduni.monsterlair.treasure

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.widget.doAfterTextChanged
import androidx.lifecycle.Observer
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import de.enduni.monsterlair.R
import de.enduni.monsterlair.common.setTextIfNotFocused
import de.enduni.monsterlair.common.view.*
import de.enduni.monsterlair.databinding.DialogTreasureFilterBinding
import de.enduni.monsterlair.treasure.view.TreasureFilterViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.koin.androidx.viewmodel.ext.android.viewModel


@ExperimentalCoroutinesApi
class TreasureFilterBottomSheet : BottomSheetDialogFragment() {

    private lateinit var binding: DialogTreasureFilterBinding

    private val viewModel: TreasureFilterViewModel by viewModel()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.dialog_treasure_filter, container, true)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        binding = DialogTreasureFilterBinding.bind(view)

        viewModel.traits.observe(
            viewLifecycleOwner,
            Observer { binding.traitsSelect.setupTraitsSelect(it, viewModel.filterStore) }
        )
        viewModel.filter.observe(viewLifecycleOwner, Observer { filter ->
            binding.treasureCategoryChips.addTreasureCategoryChips(
                filter.categories,
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
            binding.lowerGoldRangeEditText.setTextIfNotFocused(filter.lowerGoldCost)
            binding.upperGoldRangeEditText.setTextIfNotFocused(filter.upperGoldCost)
        })

        binding.treasureCategorySelect.setupTreasureCategorySelect(viewModel.filterStore)
        binding.treasureCategorySelect.adjustBottomSheetPadding(binding.root)
        binding.traitsSelect.adjustBottomSheetPadding(binding.root)

        binding.lowerGoldRangeEditText.doAfterTextChanged { text ->
            if (text.isNullOrBlank()) {
                viewModel.filterStore.setLowerGoldCost(null)
            } else {
                viewModel.filterStore.setLowerGoldCost(text.toString().toDoubleOrNull())
            }
        }
        binding.upperGoldRangeEditText.doAfterTextChanged { text ->
            if (text.isNullOrBlank()) {
                viewModel.filterStore.setUpperGoldCost(null)
            } else {
                viewModel.filterStore.setUpperGoldCost(text.toString().toDoubleOrNull())
            }
        }
    }


    companion object {

        fun newInstance() = TreasureFilterBottomSheet()

    }

}