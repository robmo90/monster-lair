package de.enduni.monsterlair.creator.view.adapter

import android.view.View
import de.enduni.monsterlair.R
import de.enduni.monsterlair.common.getStringRes
import de.enduni.monsterlair.creator.view.EncounterCreatorDisplayModel
import de.enduni.monsterlair.databinding.ViewholderEncounterBudgetBinding


class EncounterBudgetViewHolder(
    itemView: View,
    private val clickedListener: ClickListener
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

        binding.listItemCaption.text = currentDifficulty
        binding.saveButton.setOnClickListener {
            clickedListener.onSaveClicked()
        }
        binding.randomButton.setOnClickListener {
            clickedListener.onRandomClicked()
        }
    }

    interface ClickListener {
        fun onSaveClicked()
        fun onRandomClicked()
    }

}
