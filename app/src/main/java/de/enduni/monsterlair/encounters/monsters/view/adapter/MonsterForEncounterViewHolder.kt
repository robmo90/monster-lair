package de.enduni.monsterlair.encounters.monsters.view.adapter

import android.view.View
import coil.api.load
import de.enduni.monsterlair.R
import de.enduni.monsterlair.common.getIcon
import de.enduni.monsterlair.common.getStringRes
import de.enduni.monsterlair.databinding.ViewholderEncounterMonsterBinding
import de.enduni.monsterlair.encounters.monsters.view.EncounterCreatorDisplayModel


class MonsterForEncounterViewHolder(
    itemView: View,
    private val listener: MonsterForEncounterListener
) : EncounterCreatorViewHolder(itemView) {

    private lateinit var binding: ViewholderEncounterMonsterBinding

    override fun bind(displayModel: EncounterCreatorDisplayModel) {
        binding = ViewholderEncounterMonsterBinding.bind(itemView)
        val monster = displayModel as EncounterCreatorDisplayModel.MonsterForEncounter
        binding.listItemIcon.load(
            itemView.resources.getDrawable(
                monster.type.getIcon(),
                itemView.context.theme
            )
        )
        binding.listItemTitle.text = monster.name
        val caption = itemView.resources.getString(
            R.string.encounter_monster_item_caption,
            monster.level,
            monster.role.xp
        )
        binding.listItemCaption.text = caption
        binding.monsterCountTextView.text = monster.count.toString()
        binding.listItemRole.text = itemView.resources.getString(monster.role.getStringRes())

        binding.monsterCountIncrement.setOnClickListener {
            listener.onIncrement(monster.id)
        }

        binding.monsterCountDecrement.setOnClickListener {
            listener.onDecrement(monster.id)
        }
    }

    interface MonsterForEncounterListener {
        fun onIncrement(monsterId: Int)
        fun onDecrement(monsterId: Int)
    }

}
