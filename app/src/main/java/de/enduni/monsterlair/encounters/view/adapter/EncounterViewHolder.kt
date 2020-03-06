package de.enduni.monsterlair.encounters.view.adapter

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import de.enduni.monsterlair.R
import de.enduni.monsterlair.common.getStringRes
import de.enduni.monsterlair.databinding.ViewholderSavedEncounterBinding
import de.enduni.monsterlair.encounters.view.EncounterDisplayModel

class EncounterViewHolder(
    itemView: View,
    private val onClickListener: OnClickListener
) : RecyclerView.ViewHolder(itemView) {

    private lateinit var binding: ViewholderSavedEncounterBinding

    fun bind(encounter: EncounterDisplayModel) {
        binding = ViewholderSavedEncounterBinding.bind(itemView)
        binding.listItemTitle.text = encounter.name
        binding.listItemCaption.text = itemView.context.resources.getString(
            R.string.encounter_details,
            encounter.level,
            encounter.numberOfPlayers,
            itemView.context.getString(encounter.difficulty.getStringRes()),
            encounter.difficulty.budget,
            encounter.xp
        )
        binding.listItemMonsters.text = encounter.dangers
        binding.root.setOnClickListener {
            onClickListener.onEncounterSelected(encounter.id)
        }
        binding.moreButton.setOnClickListener {
            onClickListener.onEncounterOptionsSelected(encounter.id)
        }
    }


    interface OnClickListener {
        fun onEncounterSelected(id: Long)
        fun onEncounterOptionsSelected(id: Long)
    }
}