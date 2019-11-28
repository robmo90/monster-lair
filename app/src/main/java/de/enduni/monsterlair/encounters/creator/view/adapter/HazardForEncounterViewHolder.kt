package de.enduni.monsterlair.encounters.creator.view.adapter

import android.view.View
import coil.api.load
import de.enduni.monsterlair.R
import de.enduni.monsterlair.common.getIcon
import de.enduni.monsterlair.common.getStringRes
import de.enduni.monsterlair.common.getXp
import de.enduni.monsterlair.databinding.ViewholderEncounterMonsterBinding
import de.enduni.monsterlair.encounters.creator.view.EncounterCreatorDisplayModel


class HazardForEncounterViewHolder(
    itemView: View,
    private val listener: HazardForEncounterListener
) : EncounterCreatorViewHolder(itemView) {

    private lateinit var binding: ViewholderEncounterMonsterBinding

    override fun bind(displayModel: EncounterCreatorDisplayModel) {
        binding = ViewholderEncounterMonsterBinding.bind(itemView)
        val hazard = displayModel as EncounterCreatorDisplayModel.HazardForEncounter
        binding.listItemIcon.load(hazard.complexity.getIcon())
        binding.listItemTitle.text = hazard.name
        val caption = itemView.resources.getString(
            R.string.hazard_with_xp_item_caption,
            itemView.resources.getString(hazard.complexity.getStringRes()),
            hazard.level,
            hazard.role.getXp(hazard.complexity)
        )
        binding.listItemCaption.text = caption
        binding.monsterCountTextView.text = hazard.count.toString()
        binding.listItemRole.text = itemView.resources.getString(hazard.role.getStringRes())

        binding.monsterCountIncrement.setOnClickListener {
            listener.onIncrementHazard(hazard.id)
        }

        binding.monsterCountDecrement.setOnClickListener {
            listener.onDecrementHazard(hazard.id)
        }
    }

    interface HazardForEncounterListener {
        fun onIncrementHazard(hazardId: Long)
        fun onDecrementHazard(hazardId: Long)
    }

}
