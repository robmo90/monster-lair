package de.enduni.monsterlair.encounters.monsters.view.adapter

import android.view.View
import coil.api.load
import de.enduni.monsterlair.R
import de.enduni.monsterlair.common.getIcon
import de.enduni.monsterlair.databinding.ViewholderMonsterBinding
import de.enduni.monsterlair.encounters.monsters.view.EncounterCreatorDisplayModel


class MonsterViewHolder(
    itemView: View,
    private val monsterSelectedListener: MonsterViewHolderListener
) : EncounterCreatorViewHolder(itemView) {

    private lateinit var binding: ViewholderMonsterBinding

    override fun bind(displayModel: EncounterCreatorDisplayModel) {
        binding = ViewholderMonsterBinding.bind(itemView)
        val monster = displayModel as EncounterCreatorDisplayModel.Monster
        binding.listItemIcon.load(
            itemView.resources.getDrawable(
                monster.type.getIcon(),
                itemView.context.theme
            )
        )
        binding.listItemTitle.text = monster.name
        val caption = itemView.resources.getString(
            R.string.monster_with_xp_item_caption,
            monster.family,
            monster.level,
            monster.role.xp
        )
        binding.listItemCaption.text = caption

        binding.root.setOnClickListener {
            monsterSelectedListener.onSelect(monster.id)
        }
    }

    interface MonsterViewHolderListener {
        fun onSelect(id: Int)
    }

}
