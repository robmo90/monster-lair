package de.enduni.monsterlair.encounters.creator.view.adapter

import android.view.View
import coil.api.load
import de.enduni.monsterlair.R
import de.enduni.monsterlair.databinding.ViewholderDangerBinding
import de.enduni.monsterlair.encounters.creator.view.DangerType
import de.enduni.monsterlair.encounters.creator.view.EncounterCreatorDisplayModel


class DangerViewHolder(
    itemView: View,
    private val dangerSelectedListener: DangerSelectedListener
) : EncounterCreatorViewHolder(itemView) {

    private lateinit var binding: ViewholderDangerBinding

    override fun bind(displayModel: EncounterCreatorDisplayModel) {
        binding = ViewholderDangerBinding.bind(itemView)
        val danger = displayModel as EncounterCreatorDisplayModel.Danger
        binding.listItemIcon.load(
            itemView.resources.getDrawable(
                danger.icon,
                itemView.context.theme
            )
        )
        binding.listItemTitle.text = danger.name
        val caption = itemView.resources.getString(
            R.string.monster_with_xp_item_caption,
            danger.labelRes?.let { itemView.resources.getString(it) } ?: danger.label,
            danger.level,
            danger.xp
        )
        binding.listItemCaption.text = caption

        binding.root.setOnClickListener {
            dangerSelectedListener.onDangerSelected(danger.url)
        }
        binding.addButton.setOnClickListener {
            dangerSelectedListener.onAddClicked(danger.type, danger.id)
        }
    }

    interface DangerSelectedListener {
        fun onDangerSelected(url: String)
        fun onAddClicked(type: DangerType, id: Long)
    }

}
