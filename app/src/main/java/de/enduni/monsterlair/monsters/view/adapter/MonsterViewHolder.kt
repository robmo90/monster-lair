package de.enduni.monsterlair.monsters.view.adapter

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import coil.api.load
import de.enduni.monsterlair.R
import de.enduni.monsterlair.common.getIcon
import de.enduni.monsterlair.databinding.ViewholderMonsterBinding
import de.enduni.monsterlair.monsters.view.MonsterListDisplayModel


class MonsterViewHolder(
    itemView: View,
    private val monsterSelectedListener: MonsterViewHolderListener
) : RecyclerView.ViewHolder(itemView) {

    private lateinit var binding: ViewholderMonsterBinding

    fun bind(monster: MonsterListDisplayModel) {
        binding = ViewholderMonsterBinding.bind(itemView)
        binding.listItemIcon.load(
            itemView.resources.getDrawable(
                monster.type.getIcon(),
                itemView.context.theme
            )
        )
        binding.listItemTitle.text = monster.name
        val caption = itemView.resources.getString(
            R.string.monster_item_caption,
            monster.family,
            monster.level
        )
        binding.listItemCaption.text = caption

        binding.root.setOnClickListener {
            monsterSelectedListener.onSelect(monsterId = monster.id)
        }
        if (monster.custom) {
            binding.root.setOnLongClickListener {
                monsterSelectedListener.onLongPress(monster.id)
                true
            }
        }
    }

    interface MonsterViewHolderListener {
        fun onSelect(monsterId: Long)
        fun onLongPress(monsterId: Long)
    }

}
