package de.enduni.monsterlair.encounters.creator.view.adapter

import android.view.View
import de.enduni.monsterlair.common.getStringRes
import de.enduni.monsterlair.databinding.ViewholderEncounterDataBinding
import de.enduni.monsterlair.encounters.creator.view.EncounterCreatorDisplayModel


class EncounterBudgetViewHolder(
    itemView: View,
    private val onSaveClickedListener: OnSaveClickedListener
) : EncounterCreatorViewHolder(itemView) {

    private lateinit var binding: ViewholderEncounterDataBinding

    override fun bind(displayModel: EncounterCreatorDisplayModel) {
        binding = ViewholderEncounterDataBinding.bind(itemView)
        val encounter = displayModel as EncounterCreatorDisplayModel.EncounterInformation
        val currentDifficulty =
            itemView.context.resources.getString(encounter.currentDifficulty.getStringRes())
        val targetDifficulty =
            itemView.context.resources.getString(encounter.targetDifficulty.getStringRes())
        binding.listItemTitle.text = "Difficulty: $currentDifficulty - Target: $targetDifficulty"
        val caption = "Budget: ${encounter.currentBudget} / ${encounter.targetBudget} XP"
        binding.listItemCaption.text = caption
        binding.saveButton.setOnClickListener {
            onSaveClickedListener.onSaveClicked()
        }
    }

    interface OnSaveClickedListener {
        fun onSaveClicked()
    }

}
