package de.enduni.monsterlair.treasure.view.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import de.enduni.monsterlair.R
import de.enduni.monsterlair.treasure.domain.Treasure

class TreasureListAdapter(
    private val layoutInflater: LayoutInflater,
    private val monsterViewHolderListener: TreasureViewHolder.TreasureViewHolderListener
) : ListAdapter<Treasure, TreasureViewHolder>(TreasureDiffItemCallback()) {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TreasureViewHolder {
        val view = layoutInflater.inflate(R.layout.viewholder_treasure, parent, false)
        return TreasureViewHolder(view, monsterViewHolderListener)
    }

    override fun onBindViewHolder(holder: TreasureViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

}

class TreasureDiffItemCallback : DiffUtil.ItemCallback<Treasure>() {

    override fun areItemsTheSame(
        oldItem: Treasure,
        newItem: Treasure
    ): Boolean {
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(
        oldItem: Treasure,
        newItem: Treasure
    ): Boolean {
        return true
    }


}