package de.enduni.monsterlair.creator.view.adapter

import android.view.View
import coil.api.load
import de.enduni.monsterlair.R
import de.enduni.monsterlair.common.domain.Strength
import de.enduni.monsterlair.creator.view.AdjustMonsterStrengthDialog
import de.enduni.monsterlair.creator.view.DangerType
import de.enduni.monsterlair.creator.view.EncounterCreatorDisplayModel
import de.enduni.monsterlair.databinding.ViewholderEncounterMonsterBinding


class DangerForEncounterViewHolder(
    itemView: View,
    private val listener: DangerForEncounterListener,
    private val monsterStrengthListener: AdjustMonsterStrengthDialog.Listener
) : EncounterCreatorViewHolder(itemView) {

    private lateinit var binding: ViewholderEncounterMonsterBinding

    override fun bind(displayModel: EncounterCreatorDisplayModel) {
        binding = ViewholderEncounterMonsterBinding.bind(itemView)
        val dangerForEncounter = displayModel as EncounterCreatorDisplayModel.DangerForEncounter
        binding.listItemIcon.load(
            itemView.resources.getDrawable(
                dangerForEncounter.icon,
                itemView.context.theme
            )
        )
        binding.listItemTitle.text = when (dangerForEncounter.strength) {
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
        val caption = itemView.resources.getString(
            R.string.encounter_monster_item_caption,
            dangerForEncounter.level,
            dangerForEncounter.xp
        )
        binding.listItemCaption.text = caption
        binding.monsterCountTextView.text = dangerForEncounter.count.toString()
        binding.listItemRole.text = itemView.resources.getString(dangerForEncounter.role)

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

        binding.root.setOnClickListener {
            listener.onDangerForEncounterSelected(dangerForEncounter.url)
        }
        if (dangerForEncounter.type == DangerType.MONSTER) {
            binding.root.setOnLongClickListener {
                AdjustMonsterStrengthDialog().show(
                    it.context,
                    displayModel.id,
                    displayModel.strength,
                    monsterStrengthListener
                )
                true
            }
        }
    }

    interface DangerForEncounterListener {
        fun onIncrement(type: DangerType, id: String, strength: Strength)
        fun onDecrement(type: DangerType, id: String, strength: Strength)
        fun onDangerForEncounterSelected(url: String?)
        fun onCustomMonsterLongPressed(id: String, name: String)
    }

}
