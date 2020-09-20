package de.enduni.monsterlair.hazards.view.adapter

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import coil.api.load
import de.enduni.monsterlair.common.collapse
import de.enduni.monsterlair.common.expand
import de.enduni.monsterlair.common.getIcon
import de.enduni.monsterlair.databinding.ViewholderHazardBinding
import de.enduni.monsterlair.hazards.domain.Hazard
import de.enduni.monsterlair.monsters.view.adapter.TraitBuilder


class HazardViewHolder(
    itemView: View,
    private val hazardSelectedListener: HazardSelectedListener
) : RecyclerView.ViewHolder(itemView) {

    private lateinit var binding: ViewholderHazardBinding

    private var expanded = false

    fun bind(hazard: Hazard) {
        expanded = false

        binding = ViewholderHazardBinding.bind(itemView)
        binding.detailLayout.visibility = View.GONE
        binding.listItemIcon.load(
            itemView.resources.getDrawable(
                hazard.complexity.getIcon(),
                itemView.context.theme
            )
        )
        binding.listItemTitle.text = hazard.name
        binding.listItemCaption.text = hazard.level.toString()

        if (binding.traits.childCount > 0) {
            binding.traits.removeAllViews()
        }
        TraitBuilder.addTraits(
            binding.root.context,
            binding.traits,
            hazard.rarity,
            hazard.traits
        )
        binding.description.text = hazard.description

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
            hazardSelectedListener.onSelect(hazardId = hazard.id)
        }
    }

    interface HazardSelectedListener {
        fun onSelect(hazardId: String)
    }

}
