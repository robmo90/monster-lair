package de.enduni.monsterlair.creator.view.adapter

import android.view.View
import coil.api.load
import de.enduni.monsterlair.R
import de.enduni.monsterlair.common.collapse
import de.enduni.monsterlair.common.domain.Rarity
import de.enduni.monsterlair.common.domain.Source
import de.enduni.monsterlair.common.domain.Strength
import de.enduni.monsterlair.common.expand
import de.enduni.monsterlair.creator.view.AdjustMonsterStrengthDialog
import de.enduni.monsterlair.creator.view.DangerType
import de.enduni.monsterlair.creator.view.EncounterCreatorDisplayModel
import de.enduni.monsterlair.databinding.ViewholderEncounterMonsterBinding
import de.enduni.monsterlair.monsters.view.adapter.TraitBuilder


class DangerForEncounterViewHolder(
    itemView: View,
    private val listener: DangerForEncounterListener,
    private val monsterStrengthListener: AdjustMonsterStrengthDialog.Listener
) : EncounterCreatorViewHolder(itemView) {

    private lateinit var binding: ViewholderEncounterMonsterBinding
    private var expanded = false


    override fun bind(displayModel: EncounterCreatorDisplayModel) {
        expanded = false
        binding = ViewholderEncounterMonsterBinding.bind(itemView)
        binding.detailLayout.visibility = View.GONE

        val dangerForEncounter = displayModel as EncounterCreatorDisplayModel.DangerForEncounter
        binding.listItemIcon.load(
            itemView.resources.getDrawable(
                dangerForEncounter.icon,
                itemView.context.theme
            )
        )
        val text = when (dangerForEncounter.strength) {
            Strength.STANDARD -> dangerForEncounter.name
            Strength.ELITE -> itemView.resources.getString(
                R.string.elite_name_template,
                dangerForEncounter.name
            )
            Strength.WEAK -> itemView.resources.getString(
                R.string.weak_name_template,
                dangerForEncounter.name
            )
        }
        binding.listItemTitle.text = "${dangerForEncounter.count} $text"

        when (dangerForEncounter.rarity) {
            Rarity.UNCOMMON -> binding.listItemTitle.setTextColor(itemView.context.getColor(R.color.trait_text_uncommon))
            Rarity.RARE,
            Rarity.UNIQUE -> binding.listItemTitle.setTextColor(itemView.context.getColor(R.color.trait_text_rare))
            else -> binding.listItemTitle.setTextColor(itemView.context.getColor(R.color.textColor))
        }

        val caption = itemView.resources.getString(
            R.string.encounter_monster_item_caption,
            dangerForEncounter.count * dangerForEncounter.xp,
            itemView.resources.getString(dangerForEncounter.role)
        )
        binding.listItemCaption.text = caption
        binding.levelCaption.text = dangerForEncounter.level.toString()

        if (binding.traits.childCount > 0) {
            binding.traits.removeAllViews()
        }
        TraitBuilder.addTraits(
            itemView.context,
            binding.traits,
            dangerForEncounter.rarity,
            dangerForEncounter.traits,
            dangerForEncounter.alignment,
            dangerForEncounter.size
        )
        binding.description.text = dangerForEncounter.description

        binding.root.setOnClickListener {
            expanded = if (expanded) {
                binding.detailLayout.collapse()
                false
            } else {
                binding.detailLayout.expand()
                true
            }
        }

        binding.monsterCountIncrement.setOnClickListener {
            listener.onIncrement(
                dangerForEncounter.type,
                dangerForEncounter.id,
                dangerForEncounter.strength
            )
        }

        binding.monsterCountDecrement.setOnClickListener {
            listener.onDecrement(
                dangerForEncounter.type,
                dangerForEncounter.id,
                dangerForEncounter.strength
            )
        }

        if (dangerForEncounter.type == DangerType.MONSTER) {
            binding.adjustButton.visibility = View.VISIBLE
            binding.adjustButton.setOnClickListener {
                AdjustMonsterStrengthDialog().show(
                    it.context,
                    displayModel.id,
                    displayModel.strength,
                    monsterStrengthListener
                )
            }
        } else {
            binding.adjustButton.visibility = View.GONE
        }

        if (dangerForEncounter.source == Source.CUSTOM) {
            binding.archivesButton.visibility = View.GONE
            binding.editButton.visibility = View.GONE
            binding.deleteButton.visibility = View.GONE
            binding.deleteButton.setOnClickListener {
                listener.onCustomDangerDeleteClicked(
                    dangerForEncounter.type,
                    dangerForEncounter.id,
                    dangerForEncounter.name
                )
            }
            binding.editButton.setOnClickListener {
                listener.onCustomDangerEditClicked(dangerForEncounter.type, dangerForEncounter.id)
            }
        } else {
            binding.archivesButton.visibility = View.VISIBLE
            binding.editButton.visibility = View.GONE
            binding.deleteButton.visibility = View.GONE
            binding.archivesButton.setOnClickListener {
                listener.onOpenArchiveClicked(dangerForEncounter.url)
            }
        }
    }

    interface DangerForEncounterListener {
        fun onIncrement(type: DangerType, id: String, strength: Strength)
        fun onDecrement(type: DangerType, id: String, strength: Strength)
        fun onOpenArchiveClicked(url: String)
        fun onCustomDangerDeleteClicked(type: DangerType, id: String, name: String)
        fun onCustomDangerEditClicked(type: DangerType, id: String)
    }

}
