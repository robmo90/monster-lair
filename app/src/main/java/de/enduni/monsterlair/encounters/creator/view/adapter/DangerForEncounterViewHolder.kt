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
        val monster = displayModel as EncounterCreatorDisplayModel.DangerForEncounter
        binding.listItemIcon.load(monster.icon)
        binding.listItemTitle.text = monster.name
        val caption = itemView.resources.getString(
            R.string.encounter_monster_item_caption,
            monster.level,
            monster.xp
        )
        binding.listItemCaption.text = caption
        binding.monsterCountTextView.text = monster.count.toString()
        binding.listItemRole.text = itemView.resources.getString(monster.role)

        binding.monsterCountIncrement.setOnClickListener {
            listener.onIncrement(monster.type, monster.id)
        }

        binding.monsterCountDecrement.setOnClickListener {
            listener.onDecrement(monster.type, monster.id)
        }
    }

    interface DangerForEncounterListener {
        fun onIncrement(type: DangerType, id: Long)
        fun onDecrement(type: DangerType, id: Long)
    }

}
