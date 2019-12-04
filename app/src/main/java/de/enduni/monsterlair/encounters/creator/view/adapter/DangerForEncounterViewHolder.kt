package de.enduni.monsterlair.encounters.creator.view.adapter

import android.view.View
import coil.api.load
import de.enduni.monsterlair.R
import de.enduni.monsterlair.databinding.ViewholderEncounterMonsterBinding
import de.enduni.monsterlair.encounters.creator.view.DangerType
import de.enduni.monsterlair.encounters.creator.view.EncounterCreatorDisplayModel


class DangerForEncounterViewHolder(
    itemView: View,
    private val listener: DangerForEncounterListener
) : EncounterCreatorViewHolder(itemView) {

    private lateinit var binding: ViewholderEncounterMonsterBinding

    override fun bind(displayModel: EncounterCreatorDisplayModel) {
        binding = ViewholderEncounterMonsterBinding.bind(itemView)
        val dangerForEncounter = displayModel as EncounterCreatorDisplayModel.DangerForEncounter
        binding.listItemIcon.load(
            itemView.resources.getDrawable(
                dangerForEncounter.icon,
                itemView.context.theme
            )
        )
        binding.listItemTitle.text = dangerForEncounter.name
        val caption = itemView.resources.getString(
            R.string.encounter_monster_item_caption,
            dangerForEncounter.level,
            dangerForEncounter.xp
        )
        binding.listItemCaption.text = caption
        binding.monsterCountTextView.text = dangerForEncounter.count.toString()
        binding.listItemRole.text = itemView.resources.getString(dangerForEncounter.role)

        binding.monsterCountIncrement.setOnClickListener {
            listener.onIncrement(dangerForEncounter.type, dangerForEncounter.id)
        }

        binding.monsterCountDecrement.setOnClickListener {
            listener.onDecrement(dangerForEncounter.type, dangerForEncounter.id)
        }

        binding.root.setOnClickListener {
            listener.onDangerForEncounterSelected(dangerForEncounter.url)
        }
    }

    interface DangerForEncounterListener {
        fun onIncrement(type: DangerType, id: Long)
        fun onDecrement(type: DangerType, id: Long)
        fun onDangerForEncounterSelected(url: String)
    }

}
