package de.enduni.monsterlair.encounters.creator.view.adapter

import android.view.View
import de.enduni.monsterlair.R
import de.enduni.monsterlair.common.getStringRes
import de.enduni.monsterlair.databinding.ViewholderEncounterBudgetBinding
import de.enduni.monsterlair.encounters.creator.view.EncounterCreatorDisplayModel


class EncounterBudgetViewHolder(
    itemView: View,
    private val onSaveClickedListener: OnSaveClickedListener
) : EncounterCreatorViewHolder(itemView) {

    private lateinit var binding: ViewholderEncounterBudgetBinding

    override fun bind(displayModel: EncounterCreatorDisplayModel) {
        binding = ViewholderEncounterBudgetBinding.bind(itemView)
        val encounter = displayModel as EncounterCreatorDisplayModel.EncounterBudget
        val currentDifficulty =
            itemView.context.resources.getString(encounter.currentDifficulty.getStringRes())

        binding.listItemTitle.text = itemView.context.getString(
            R.string.encounter_budget_xp,
            encounter.currentBudget,
            encounter.targetBudget
        )

        binding.listItemCaption.text = itemView.context.getString(
            R.string.encounter_budget_difficulty,
            currentDifficulty
        )
        binding.saveButton.setOnClickListener {
            onSaveClickedListener.onSaveClicked()
        }
    }

    interface OnSaveClickedListener {
        fun onSaveClicked()
    }

}
