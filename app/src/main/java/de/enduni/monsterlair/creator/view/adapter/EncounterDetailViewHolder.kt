package de.enduni.monsterlair.creator.view.adapter

import android.view.View
import de.enduni.monsterlair.R
import de.enduni.monsterlair.common.getStringRes
import de.enduni.monsterlair.creator.view.EncounterCreatorDisplayModel
import de.enduni.monsterlair.databinding.ViewholderEncounterBudgetBinding


class EncounterDetailViewHolder(
    itemView: View,
    private val clickedListener: ClickListener
) : EncounterCreatorViewHolder(itemView) {

    private lateinit var binding: ViewholderEncounterBudgetBinding

    override fun bind(displayModel: EncounterCreatorDisplayModel) {
        binding = ViewholderEncounterBudgetBinding.bind(itemView)
        val budget = displayModel as EncounterCreatorDisplayModel.EncounterDetails

        binding.listItemTitle.text = itemView.context.getString(
            R.string.encounter_budget_xp,
            budget.currentBudget,
            budget.targetBudget
        )

        binding.listItemCaption.text = itemView.context.getString(
            R.string.encounter_details_creator,
            budget.level,
            budget.numberOfPlayers,
            itemView.context.resources.getString(displayModel.targetDifficulty.getStringRes())
        )
        binding.saveButton.setOnClickListener {
            clickedListener.onSaveClicked()
        }
    }

    interface ClickListener {
        fun onSaveClicked()
    }

}
