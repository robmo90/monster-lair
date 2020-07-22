package de.enduni.monsterlair.monsters

import android.app.Dialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import androidx.lifecycle.Observer
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import de.enduni.monsterlair.R
import de.enduni.monsterlair.common.view.*
import de.enduni.monsterlair.databinding.DialogMonsterFilterBinding
import de.enduni.monsterlair.monsters.view.MonsterFilterViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.koin.androidx.viewmodel.ext.android.viewModel


@ExperimentalCoroutinesApi
class MonsterFilterBottomSheet : BottomSheetDialogFragment() {

    private lateinit var binding: DialogMonsterFilterBinding

    private val viewModel: MonsterFilterViewModel by viewModel()


    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val dialog = BottomSheetDialog(requireActivity(), R.style.DialogStyle)

        dialog.setOnShowListener { d ->
            // of a dialog in order to change their Typeface. Good ol' days.
            val bottomSheet =
                (d as BottomSheetDialog).findViewById<FrameLayout>(com.google.android.material.R.id.design_bottom_sheet)
            BottomSheetBehavior.from(bottomSheet!!)
                .setState(BottomSheetBehavior.STATE_EXPANDED)
        }
        return dialog
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.dialog_monster_filter, container, true)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        binding = DialogMonsterFilterBinding.bind(view)

        viewModel.traits.observe(
            viewLifecycleOwner,
            Observer { binding.traitsSelect.setupTraitsSelect(it, viewModel.filterStore) }
        )
        viewModel.filter.observe(viewLifecycleOwner, Observer { filter ->
            binding.monsterTypeChips.addMonsterTypeChips(
                filter.types,
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

        binding.monsterTypeSelect.setupMonsterTypeSelect(viewModel.filterStore)
        binding.alignmentSelect.setupAlignmentSelect(viewModel.filterStore)
        binding.sizeSelect.setupSizeSelect(viewModel.filterStore)

        binding.traitsSelect.adjustBottomSheetPadding(binding.root)
        binding.monsterTypeSelect.adjustBottomSheetPadding(binding.root)
        binding.alignmentSelect.adjustBottomSheetPadding(binding.root)
        binding.sizeSelect.adjustBottomSheetPadding(binding.root)
    }


    companion object {

        fun newInstance() = MonsterFilterBottomSheet()

    }

}