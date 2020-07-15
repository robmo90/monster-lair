package de.enduni.monsterlair.treasure.view.adapter

import android.net.Uri
import android.view.View
import androidx.recyclerview.widget.RecyclerView
import coil.api.load
import de.enduni.monsterlair.common.openCustomTab
import de.enduni.monsterlair.databinding.ViewholderTreasureBinding
import de.enduni.monsterlair.treasure.view.TreasureDisplayModel


class TreasureViewHolder(
    itemView: View,
    private val listener: TreasureViewHolderListener
) : RecyclerView.ViewHolder(itemView) {

    private lateinit var binding: ViewholderTreasureBinding

    fun bind(treasure: TreasureDisplayModel) {
        binding = ViewholderTreasureBinding.bind(itemView)
        binding.listItemIcon.load(
            itemView.resources.getDrawable(
                treasure.icon,
                itemView.context.theme
            )
        )
        binding.listItemTitle.text = treasure.name
        binding.listItemCaption.text = treasure.caption

        binding.root.setOnClickListener {
            Uri.parse(treasure.url).openCustomTab(itemView.context)
        }
    }

    interface TreasureViewHolderListener {
        fun onSelect(monsterId: String)
    }

}
