package de.enduni.monsterlair.monsters.view.adapter

import android.content.Intent
import android.net.Uri
import android.view.View
import androidx.recyclerview.widget.RecyclerView
import coil.api.load
import de.enduni.monsterlair.R
import de.enduni.monsterlair.databinding.ViewholderMonsterBinding
import de.enduni.monsterlair.monsters.domain.MonsterType
import de.enduni.monsterlair.monsters.view.MonsterListDisplayModel


class MonsterViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

    private lateinit var binding: ViewholderMonsterBinding

    fun bind(monster: MonsterListDisplayModel) {
        binding = ViewholderMonsterBinding.bind(itemView)
        binding.listItemIcon.load(
            itemView.resources.getDrawable(
                monster.getIcon(),
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
            val intent = Intent(Intent.ACTION_VIEW)
            intent.data = Uri.parse(monster.url)
            itemView.context.startActivity(intent)
        }
    }

    private fun MonsterListDisplayModel.getIcon(): Int {
        return when (this.type) {
            MonsterType.ABERRATION -> R.drawable.ic_monster_aberration
            MonsterType.ANIMAL -> R.drawable.ic_monster_animal
            MonsterType.BEAST -> R.drawable.ic_monster_beast
            MonsterType.DRAGON -> R.drawable.ic_monster_dragon
            MonsterType.ELEMENTAL -> R.drawable.ic_monster_elemental
            MonsterType.FIEND -> R.drawable.ic_monster_fiend
            MonsterType.GIANT -> R.drawable.ic_monster_giant
            MonsterType.HUMANOID -> R.drawable.ic_monster_humanoid
            MonsterType.MONITOR -> R.drawable.ic_monster_monitor
            MonsterType.OOZE -> R.drawable.ic_monster_ooze
            MonsterType.SPIRIT -> R.drawable.ic_monster_spirit
            MonsterType.UNDEAD -> R.drawable.ic_monster_undead
            MonsterType.ASTRAL -> R.drawable.ic_monster_astral
            MonsterType.CELESTIAL -> R.drawable.ic_monster_celestial
            MonsterType.CONSTRUCT -> R.drawable.ic_monster_construct
            MonsterType.ETHEREAL -> R.drawable.ic_monster_ethereal
            MonsterType.FEY -> R.drawable.ic_monster_fey
            MonsterType.FUNGUS -> R.drawable.ic_monster_fungus
            MonsterType.PLANT -> R.drawable.ic_monster_plant
            MonsterType.NONE -> R.drawable.ic_monster_ooze
        }
    }
}
