package de.enduni.monsterlair.creator.view.adapter

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import de.enduni.monsterlair.creator.view.EncounterCreatorDisplayModel

abstract class EncounterCreatorViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

    abstract fun bind(displayModel: EncounterCreatorDisplayModel)

}