package de.enduni.monsterlair.encounters.view.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import de.enduni.monsterlair.R
import de.enduni.monsterlair.encounters.view.EncounterDisplayModel

class EncounterListAdapter(
    private val layoutInflater: LayoutInflater,
    private val onClickListener: EncounterViewHolder.OnClickListener
) : ListAdapter<EncounterDisplayModel, EncounterViewHolder>(EncounterDiffItemCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): EncounterViewHolder {
        val view = layoutInflater.inflate(R.layout.viewholder_saved_encounter, parent, false)
        return EncounterViewHolder(view, onClickListener)
    }

    override fun onBindViewHolder(holder: EncounterViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

}

class EncounterDiffItemCallback : DiffUtil.ItemCallback<EncounterDisplayModel>() {

    override fun areItemsTheSame(
        oldItem: EncounterDisplayModel,
        newItem: EncounterDisplayModel
    ): Boolean {
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(
        oldItem: EncounterDisplayModel,
        newItem: EncounterDisplayModel
    ): Boolean {
        return oldItem == newItem
    }


}