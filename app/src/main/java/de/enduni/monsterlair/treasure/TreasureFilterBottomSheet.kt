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
import de.enduni.monsterlair.treasure.view.TreasureFilterDialogViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.koin.androidx.viewmodel.ext.android.viewModel


@ExperimentalCoroutinesApi
class TreasureFilterBottomSheet : BottomSheetDialogFragment() {

    private lateinit var binding: DialogTreasureFilterBinding

    private val viewModel: TreasureFilterDialogViewModel by viewModel()

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
            Observer {
                binding.traitsSelect.setupTraitsSelect(it) { trait ->
                    viewModel.addTrait(trait)
                }
            })
        viewModel.filter.observe(viewLifecycleOwner, Observer { filter ->
            binding.treasureCategoryChips.addTreasureCategoryChips(
                filter.categories,
                clear = true
            ) { viewModel.removeTreasureCategory(it) }
            binding.traitsChips.addTraitChips(
                filter.traits,
                clear = true
            ) { viewModel.removeTrait(it) }
            binding.rarityChips.setupRarityChips(
                filter.rarities,
                addAction = { viewModel.addRarity(it) },
                removeAction = { viewModel.removeRarity(it) })
            binding.lowerGoldRangeEditText.setTextIfNotFocused(filter.lowerGoldCost)
            binding.upperGoldRangeEditText.setTextIfNotFocused(filter.upperGoldCost)
        })

        binding.treasureCategorySelect.setupTreasureCategorySelect {
            viewModel.addTreasureCategory(it)
        }
        binding.treasureCategorySelect.adjustBottomSheetPadding(binding.root)
        binding.traitsSelect.adjustBottomSheetPadding(binding.root)

        binding.lowerGoldRangeEditText.doAfterTextChanged { text ->
            if (text.isNullOrBlank()) {
                viewModel.setLowerGoldCost(null)
            } else {
                viewModel.setLowerGoldCost(text.toString().toDoubleOrNull())
            }
        }
        binding.upperGoldRangeEditText.doAfterTextChanged { text ->
            if (text.isNullOrBlank()) {
                viewModel.setUpperGoldCost(null)
            } else {
                viewModel.setUpperGoldCost(text.toString().toDoubleOrNull())
            }
        }
    }


    companion object {

        fun newInstance() = TreasureFilterBottomSheet()

    }

}