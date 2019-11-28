package de.enduni.monsterlair.encounters.creator.view.adapter

import android.view.View
import coil.api.load
import de.enduni.monsterlair.R
import de.enduni.monsterlair.common.getIcon
import de.enduni.monsterlair.common.getStringRes
import de.enduni.monsterlair.common.getXp
import de.enduni.monsterlair.databinding.ViewholderHazardBinding
import de.enduni.monsterlair.encounters.creator.view.EncounterCreatorDisplayModel


class HazardViewHolder(
    itemView: View,
    private val monsterSelectedListener: HazardSelectedListener
) : EncounterCreatorViewHolder(itemView) {

    private lateinit var binding: ViewholderHazardBinding

    override fun bind(displayModel: EncounterCreatorDisplayModel) {
        binding = ViewholderHazardBinding.bind(itemView)
        val hazard = displayModel as EncounterCreatorDisplayModel.Hazard
        binding.listItemIcon.load(hazard.complexity.getIcon())

        binding.listItemTitle.text = hazard.name
        val caption = itemView.resources.getString(
            R.string.hazard_with_xp_item_caption,
            itemView.resources.getString(hazard.complexity.getStringRes()),
            hazard.level,
            hazard.role.getXp(hazard.complexity)
        )
        binding.listItemCaption.text = caption

        binding.root.setOnClickListener {
            monsterSelectedListener.onSelectHazard(hazard.id)
        }
    }

    interface HazardSelectedListener {

        fun onSelectHazard(id: Long)
    }


}
