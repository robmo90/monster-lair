package de.enduni.monsterlair.creator.view.adapter

import android.view.View
import androidx.core.widget.doAfterTextChanged
import de.enduni.monsterlair.R
import de.enduni.monsterlair.common.getStringRes
import de.enduni.monsterlair.common.setTextIfNotFocused
import de.enduni.monsterlair.creator.view.EncounterCreatorDisplayModel
import de.enduni.monsterlair.databinding.ViewholderEncounterDetailBinding


class EncounterDetailViewHolder(
    itemView: View,
    private val onNameChangedListener: OnNameChangedListener
) : EncounterCreatorViewHolder(itemView) {

    private lateinit var binding: ViewholderEncounterDetailBinding

    override fun bind(displayModel: EncounterCreatorDisplayModel) {
        binding = ViewholderEncounterDetailBinding.bind(itemView)
        val encounter = displayModel as EncounterCreatorDisplayModel.EncounterDetail
        val targetDifficulty =
            itemView.context.resources.getString(encounter.targetDifficulty.getStringRes())

        binding.encounterName.setTextIfNotFocused(
            encounter.name
        )

        binding.listItemCaption.text = itemView.context.getString(
            R.string.encounter_details_creator,
            encounter.level,
            encounter.numberOfPlayers,
            targetDifficulty
        )
        binding.encounterName.doAfterTextChanged { editable ->
            onNameChangedListener.onNameChanged(editable.toString())
        }
    }


    interface OnNameChangedListener {
        fun onNameChanged(name: String)
    }

}
