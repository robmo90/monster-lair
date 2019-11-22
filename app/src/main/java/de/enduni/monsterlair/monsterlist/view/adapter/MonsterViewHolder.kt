package de.enduni.monsterlair.monsterlist.view.adapter

import android.content.Intent
import android.net.Uri
import android.view.View
import androidx.recyclerview.widget.RecyclerView
import coil.api.load
import de.enduni.monsterlair.R
import de.enduni.monsterlair.databinding.ViewholderMonsterBinding
import de.enduni.monsterlair.monsterlist.view.MonsterListDisplayModel


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
        return when (this.type.toLowerCase()) {
            "aberration" -> R.drawable.ic_monster_aberration
            "animal" -> R.drawable.ic_monster_animal
            "beast" -> R.drawable.ic_monster_beast
            "dragon" -> R.drawable.ic_monster_dragon
            "elemental" -> R.drawable.ic_monster_elemental
            "fiend" -> R.drawable.ic_monster_fiend
            "giant" -> R.drawable.ic_monster_giant
            "humanoid" -> R.drawable.ic_monster_humanoid
            "monitor" -> R.drawable.ic_monster_monitor
            "ooze" -> R.drawable.ic_monster_ooze
            "spirit" -> R.drawable.ic_monster_spirit
            "undead" -> R.drawable.ic_monster_undead
            "astral" -> R.drawable.ic_monster_astral
            "celestial" -> R.drawable.ic_monster_celestial
            "construct" -> R.drawable.ic_monster_construct
            "ethereal" -> R.drawable.ic_monster_ethereal
            "fey" -> R.drawable.ic_monster_fey
            "fungus" -> R.drawable.ic_monster_fungus
            "plant" -> R.drawable.ic_monster_plant
            else -> android.R.drawable.ic_lock_idle_lock
        }
    }
}
