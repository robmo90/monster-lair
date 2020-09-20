package de.enduni.monsterlair.common.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.widget.doAfterTextChanged
import androidx.fragment.app.DialogFragment
import androidx.lifecycle.Observer
import de.enduni.monsterlair.R
import de.enduni.monsterlair.common.domain.Alignment
import de.enduni.monsterlair.common.domain.MonsterType
import de.enduni.monsterlair.common.domain.Rarity
import de.enduni.monsterlair.common.domain.Size
import de.enduni.monsterlair.common.getStringRes
import de.enduni.monsterlair.common.setTextIfNotFocused
import de.enduni.monsterlair.common.view.filterchips.RemovableFilterChip
import de.enduni.monsterlair.databinding.DialogCreateMonsterBinding
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.koin.androidx.viewmodel.ext.android.viewModel
import timber.log.Timber

@ExperimentalCoroutinesApi
class CreateMonsterDialog() : DialogFragment() {

    private lateinit var binding: DialogCreateMonsterBinding

    private val viewModel: CreateMonsterViewModel by viewModel()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setStyle(STYLE_NO_FRAME, R.style.AppTheme);
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.dialog_create_monster, container, true)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = DialogCreateMonsterBinding.bind(view)

        val monsterId = arguments?.getString(EXTRA_MONSTER_ID)
        binding.toolbar.setNavigationIcon(R.drawable.ic_close)
        binding.toolbar.setNavigationOnClickListener { dismiss() }
        Timber.d("Monster  ID: $monsterId")
        if (monsterId != null) {
            viewModel.loadMonster(monsterId)
            binding.toolbar.title =
                requireContext().resources.getString(R.string.custom_monster_edit_title)
        }


        val types = requireContext().resources.getStringArray(R.array.type_choices)
        binding.monsterTypeSelect.setupDropdown(types) { index -> viewModel.changeType(MonsterType.values()[index]) }

        val alignments = requireContext().resources.getStringArray(R.array.alignments)
        binding.monsterAlignmentSelect.setupDropdown(alignments) { index ->
            viewModel.changeAlignment(
                Alignment.values()[index]
            )
        }

        val sizes = requireContext().resources.getStringArray(R.array.sizes)
        binding.monsterSizeSelect.setupDropdown(sizes) { index -> viewModel.changeSize(Size.values()[index]) }

        val rarities = requireContext().resources.getStringArray(R.array.rarities)
        binding.monsterRaritySelect.setupDropdown(rarities) { index -> viewModel.changeRarity(Rarity.values()[index]) }

        viewModel.traits.observe(viewLifecycleOwner, Observer { traits ->
            binding.traitsSelect.setupDropdown(traits.toTypedArray()) { index ->
                viewModel.addTrait(
                    traits[index]
                )
            }
        })

        binding.monsterNameEditText.doAfterTextChanged { viewModel.changeName(it.toString()) }
        binding.monsterLevelEditText.doAfterTextChanged {
            it.toString().toIntOrNull()?.let { level -> viewModel.changeLevel(level) }
        }
        binding.monsterFamilyEditText.doAfterTextChanged { viewModel.changeFamily(it.toString()) }
        binding.notesEditText.doAfterTextChanged { viewModel.changeDescription(it.toString()) }
        binding.customTraitsEditText.doAfterTextChanged { viewModel.changeCustomTraits(it.toString()) }

        viewModel.monster.observe(viewLifecycleOwner, Observer { monster ->
            Timber.d("$monster")
            binding.monsterNameEditText.setTextIfNotFocused(monster.name)
            binding.monsterLevelEditText.setTextIfNotFocused(monster.level)
            binding.monsterFamilyEditText.setTextIfNotFocused(monster.family)
            binding.notesEditText.setTextIfNotFocused(monster.description)
            binding.monsterTypeSelect.setText(monster.type.getStringRes())
            binding.monsterAlignmentSelect.setText(monster.alignment.getStringRes())
            binding.monsterSizeSelect.setText(monster.size.getStringRes())
            binding.monsterRaritySelect.setText(monster.rarity.getStringRes())

            binding.traitsChips.removeAllViews()
            monster.traits.forEach { trait ->
                val chip = RemovableFilterChip(
                    requireContext(),
                    trait
                ) { viewModel.removeTrait(trait) }
                binding.traitsChips.addView(chip)
            }
        })

        binding.closeButton.setOnClickListener {
            viewModel.saveMonster()
        }
    }

    override fun onResume() {
        super.onResume()
        Timber.d("Resuming")
        viewModel.actions.observe(this, Observer { handleAction(it) })
    }

    private fun handleAction(event: CreateMonsterEvent) {
        binding.monsterLevelEditTextLayout.error = ""
        binding.monsterNameTextLayout.error = ""
        when (event) {
            is CreateMonsterEvent.SavedSuccessfully -> dismiss()
            is CreateMonsterEvent.Error -> {
                if (event.errors.contains(ValidationError.LEVEL)) {
                    binding.monsterLevelEditTextLayout.error =
                        requireContext().getString(R.string.enter_valid_level)
                }
                if (event.errors.contains(ValidationError.NAME)) {
                    binding.monsterNameTextLayout.error =
                        requireContext().getString(R.string.custom_monster_create_empty_name)
                }
            }

        }
    }

    override fun onPause() {
        super.onPause()
        viewModel.actions.removeObservers(this)
    }

    companion object {

        private const val EXTRA_MONSTER_ID = "monsterId"

        fun newInstance(monsterId: String?) = CreateMonsterDialog().apply {
            arguments = Bundle().apply {
                putString(EXTRA_MONSTER_ID, monsterId)
            }
        }

    }

}