package de.enduni.monsterlair.creator.view.adapter

import android.view.View
import coil.api.load
import de.enduni.monsterlair.R
import de.enduni.monsterlair.common.collapse
import de.enduni.monsterlair.common.domain.Rarity
import de.enduni.monsterlair.common.domain.Source
import de.enduni.monsterlair.common.expand
import de.enduni.monsterlair.creator.view.DangerType
import de.enduni.monsterlair.creator.view.EncounterCreatorDisplayModel
import de.enduni.monsterlair.databinding.ViewholderDangerBinding
import de.enduni.monsterlair.monsters.view.adapter.TraitBuilder


class DangerViewHolder(
    itemView: View,
    private val dangerSelectedListener: DangerListener
) : EncounterCreatorViewHolder(itemView) {

    private lateinit var binding: ViewholderDangerBinding
    private var expanded = false

    override fun bind(displayModel: EncounterCreatorDisplayModel) {
        expanded = false
        binding = ViewholderDangerBinding.bind(itemView)

        val danger = displayModel as EncounterCreatorDisplayModel.Danger
        binding.detailLayout.visibility = View.GONE
        binding.listItemIcon.load(
            itemView.resources.getDrawable(
                danger.icon,
                itemView.context.theme
            )
        )
        binding.listItemTitle.text = danger.name

        when (danger.rarity) {
            Rarity.UNCOMMON -> binding.listItemTitle.setTextColor(context.getColor(R.color.trait_text_uncommon))
            Rarity.RARE,
            Rarity.UNIQUE -> binding.listItemTitle.setTextColor(context.getColor(R.color.trait_text_rare))
            else -> binding.listItemTitle.setTextColor(context.getColor(R.color.textColor))
        }

        binding.listItemCaption.text = danger.level.toString()

        if (binding.traits.childCount > 0) {
            binding.traits.removeAllViews()
        }
        TraitBuilder.addTraits(
            context,
            binding.traits,
            danger.rarity,
            danger.traits,
            danger.alignment,
            danger.size
        )
        binding.details.text = context.getString(
            R.string.encounter_danger_caption,
            danger.xp,
            context.getString(danger.roleDescription)
        )

        binding.description.text = danger.description

        binding.root.setOnClickListener {
            expanded = if (expanded) {
                binding.detailLayout.collapse()
                false
            } else {
                binding.detailLayout.expand()
                true
            }
        }

        binding.addButton.setOnClickListener {
            dangerSelectedListener.onAddClicked(type = danger.type, id = danger.id)
        }
        binding.archivesButton.setOnClickListener {

        }

        if (danger.source == Source.CUSTOM) {
            binding.archivesButton.visibility = View.GONE
            binding.editButton.visibility = View.VISIBLE
            binding.deleteButton.visibility = View.VISIBLE
            binding.deleteButton.setOnClickListener {
                dangerSelectedListener.onCustomDangerDeleteClicked(
                    danger.type,
                    danger.id,
                    danger.name
                )
            }
            binding.editButton.setOnClickListener {
                dangerSelectedListener.onCustomDangerEditClicked(danger.type, danger.id)
            }
        } else {
            binding.archivesButton.visibility = View.VISIBLE
            binding.editButton.visibility = View.GONE
            binding.deleteButton.visibility = View.GONE
            binding.archivesButton.setOnClickListener {
                dangerSelectedListener.onOpenArchiveClicked(danger.url)
            }
        }
    }


    private val context
        get() = binding.root.context

    interface DangerListener {
        fun onOpenArchiveClicked(url: String)
        fun onAddClicked(type: DangerType, id: String)
        fun onCustomDangerDeleteClicked(type: DangerType, id: String, name: String)
        fun onCustomDangerEditClicked(type: DangerType, id: String)
    }

}
