package de.enduni.monsterlair.hazards.view.adapter

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import coil.api.load
import de.enduni.monsterlair.R
import de.enduni.monsterlair.common.getIcon
import de.enduni.monsterlair.common.getStringRes
import de.enduni.monsterlair.databinding.ViewholderHazardBinding
import de.enduni.monsterlair.hazards.view.HazardDisplayModel


class HazardViewHolder(
    itemView: View,
    private val hazardSelectedListener: HazardSelectedListener
) : RecyclerView.ViewHolder(itemView) {

    private lateinit var binding: ViewholderHazardBinding

    fun bind(hazard: HazardDisplayModel) {
        binding = ViewholderHazardBinding.bind(itemView)
        binding.listItemIcon.load(hazard.complexity.getIcon())
        binding.listItemTitle.text = hazard.name
        val caption = itemView.resources.getString(
            R.string.hazard_item_caption,
            itemView.resources.getString(hazard.complexity.getStringRes()),
            hazard.level
        )
        binding.listItemCaption.text = caption

        binding.root.setOnClickListener {
            hazardSelectedListener.onSelect(hazardId = hazard.id)
        }
    }

    interface HazardSelectedListener {
        fun onSelect(hazardId: Long)
    }

}
