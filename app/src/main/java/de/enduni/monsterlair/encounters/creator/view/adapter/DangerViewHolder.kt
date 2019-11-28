package de.enduni.monsterlair.encounters.creator.view.adapter

import android.view.View
import coil.api.load
import de.enduni.monsterlair.R
import de.enduni.monsterlair.databinding.ViewholderMonsterBinding
import de.enduni.monsterlair.encounters.creator.view.DangerType
import de.enduni.monsterlair.encounters.creator.view.EncounterCreatorDisplayModel


class DangerViewHolder(
    itemView: View,
    private val monsterSelectedListener: DangerSelectedListener
) : EncounterCreatorViewHolder(itemView) {

    private lateinit var binding: ViewholderMonsterBinding

    override fun bind(displayModel: EncounterCreatorDisplayModel) {
        binding = ViewholderMonsterBinding.bind(itemView)
        val danger = displayModel as EncounterCreatorDisplayModel.Danger
        binding.listItemIcon.load(danger.icon)
        binding.listItemTitle.text = danger.name
        val caption = itemView.resources.getString(
            R.string.monster_with_xp_item_caption,
            danger.labelRes?.let { itemView.resources.getString(it) } ?: danger.label,
            danger.level,
            danger.xp
        )
        binding.listItemCaption.text = caption

        binding.root.setOnClickListener {
            monsterSelectedListener.onSelected(danger.type, danger.id)
        }
    }

    interface DangerSelectedListener {
        fun onSelected(type: DangerType, id: Long)
    }

}
