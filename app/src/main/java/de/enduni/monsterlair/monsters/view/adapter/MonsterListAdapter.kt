package de.enduni.monsterlair.monsters.view.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import de.enduni.monsterlair.R
import de.enduni.monsterlair.monsters.view.MonsterListDisplayModel

class MonsterListAdapter(
    private val layoutInflater: LayoutInflater
) : ListAdapter<MonsterListDisplayModel, MonsterViewHolder>(MonsterDiffItemCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MonsterViewHolder {
        val view = layoutInflater.inflate(R.layout.viewholder_monster, parent, false)
        return MonsterViewHolder(view)
    }

    override fun onBindViewHolder(holder: MonsterViewHolder, position: Int) {
        holder.bind(getItem(position))
    }


}

class MonsterDiffItemCallback : DiffUtil.ItemCallback<MonsterListDisplayModel>() {

    override fun areItemsTheSame(
        oldItem: MonsterListDisplayModel,
        newItem: MonsterListDisplayModel
    ): Boolean {
        return oldItem.name == newItem.name
    }

    override fun areContentsTheSame(
        oldItem: MonsterListDisplayModel,
        newItem: MonsterListDisplayModel
    ): Boolean {
        return true
    }


}