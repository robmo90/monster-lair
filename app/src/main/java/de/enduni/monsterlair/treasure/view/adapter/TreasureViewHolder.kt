package de.enduni.monsterlair.treasure.view.adapter

import android.net.Uri
import android.view.View
import androidx.recyclerview.widget.RecyclerView
import coil.api.load
import de.enduni.monsterlair.R
import de.enduni.monsterlair.common.collapse
import de.enduni.monsterlair.common.domain.Rarity
import de.enduni.monsterlair.common.expand
import de.enduni.monsterlair.common.getIcon
import de.enduni.monsterlair.common.openCustomTab
import de.enduni.monsterlair.databinding.ViewholderTreasureBinding
import de.enduni.monsterlair.monsters.view.adapter.TraitBuilder
import de.enduni.monsterlair.treasure.domain.Treasure


class TreasureViewHolder(
    itemView: View,
    private val listener: TreasureViewHolderListener
) : RecyclerView.ViewHolder(itemView) {

    private lateinit var binding: ViewholderTreasureBinding

    private var expanded = false

    fun bind(treasure: Treasure) {
        expanded = false
        binding = ViewholderTreasureBinding.bind(itemView)

        binding.detailLayout.visibility = View.GONE
        binding.listItemIcon.load(
            itemView.resources.getDrawable(
                treasure.category.getIcon(),
                itemView.context.theme
            )
        )
        binding.listItemTitle.text = treasure.name

        when (treasure.rarity) {
            Rarity.UNCOMMON -> binding.listItemTitle.setTextColor(context.getColor(R.color.trait_text_uncommon))
            Rarity.RARE,
            Rarity.UNIQUE -> binding.listItemTitle.setTextColor(context.getColor(R.color.trait_text_rare))
            else -> binding.listItemTitle.setTextColor(context.getColor(R.color.textColor))
        }

        binding.listItemCaption.text = treasure.level.toString()

        if (binding.traits.childCount > 0) {
            binding.traits.removeAllViews()
        }
        TraitBuilder.addTraits(
            context,
            binding.traits,
            treasure.rarity,
            treasure.traits
        )

        binding.root.setOnClickListener {
            expanded = if (expanded) {
                binding.detailLayout.collapse()
                false
            } else {
                binding.detailLayout.expand()
                true
            }
        }

        binding.archivesButton.setOnClickListener {
            Uri.parse(treasure.url).openCustomTab(itemView.context)
        }
    }

    private val context
        get() = binding.root.context


    interface TreasureViewHolderListener {
        fun onSelect(monsterId: String)
    }

}
