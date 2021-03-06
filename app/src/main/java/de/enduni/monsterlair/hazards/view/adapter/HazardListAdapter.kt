package de.enduni.monsterlair.hazards.view.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import de.enduni.monsterlair.R
import de.enduni.monsterlair.hazards.domain.Hazard

class HazardListAdapter(
    private val layoutInflater: LayoutInflater,
    private val hazardSelectedListener: HazardViewHolder.HazardSelectedListener
) : ListAdapter<Hazard, HazardViewHolder>(HazardDiffItemCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HazardViewHolder {
        val view = layoutInflater.inflate(R.layout.viewholder_hazard, parent, false)
        return HazardViewHolder(view, hazardSelectedListener)
    }

    override fun onBindViewHolder(holder: HazardViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

}

class HazardDiffItemCallback : DiffUtil.ItemCallback<Hazard>() {

    override fun areItemsTheSame(
        oldItem: Hazard,
        newItem: Hazard
    ): Boolean {
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(
        oldItem: Hazard,
        newItem: Hazard
    ): Boolean {
        return true
    }


}