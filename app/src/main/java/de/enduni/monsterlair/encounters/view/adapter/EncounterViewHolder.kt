package de.enduni.monsterlair.encounters.view.adapter

import android.view.View
import androidx.recyclerview.widget.RecyclerView
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
        binding.listItemCaption.text = "${encounter.budget} XP - ${encounter.dangers}"
        binding.root.setOnClickListener {
            onClickListener.onEncounterSelected(encounter.id)
        }
    }


    interface OnClickListener {
        fun onEncounterSelected(id: Long)
    }
}