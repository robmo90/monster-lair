package de.enduni.monsterlair.encounters.monsters.view.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import de.enduni.monsterlair.R
import de.enduni.monsterlair.encounters.monsters.view.EncounterCreatorDisplayModel
import timber.log.Timber

class EncounterCreatorListAdapter(
    private val layoutInflater: LayoutInflater,
    private val monsterForEncounterListener: MonsterForEncounterViewHolder.MonsterForEncounterListener,
    private val monsterViewHolderListener: MonsterViewHolder.MonsterViewHolderListener
) : ListAdapter<EncounterCreatorDisplayModel, EncounterCreatorViewHolder>(
    EncounterCreatorDiffCallback()
) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): EncounterCreatorViewHolder {
        return when (viewType) {
            TYPE_MONSTER -> {
                val view = layoutInflater.inflate(R.layout.viewholder_monster, parent, false)
                MonsterViewHolder(view, monsterViewHolderListener)
            }
            TYPE_ENCOUNTER -> {
                Timber.d("I have an encounter")
                val view = layoutInflater.inflate(R.layout.viewholder_encounter_data, parent, false)
                EncounterBudgetViewHolder(view)
            }
            TYPE_ENCOUNTER_MONSTER -> {
                Timber.d("I have an encounter monster")
                val view =
                    layoutInflater.inflate(R.layout.viewholder_encounter_monster, parent, false)
                MonsterForEncounterViewHolder(view, monsterForEncounterListener)
            }
            else -> throw RuntimeException("Something went wrong")
        }

    }

    override fun onBindViewHolder(holder: EncounterCreatorViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    override fun getItemViewType(position: Int): Int {
        return when (getItem(position)) {
            is EncounterCreatorDisplayModel.Monster -> TYPE_MONSTER
            is EncounterCreatorDisplayModel.EncounterInformation -> TYPE_ENCOUNTER
            is EncounterCreatorDisplayModel.MonsterForEncounter -> TYPE_ENCOUNTER_MONSTER
        }
    }


    companion object {
        const val TYPE_MONSTER = 0
        const val TYPE_ENCOUNTER = 1
        const val TYPE_ENCOUNTER_MONSTER = 2
    }
}

class EncounterCreatorDiffCallback : DiffUtil.ItemCallback<EncounterCreatorDisplayModel>() {

    override fun areItemsTheSame(
        oldItem: EncounterCreatorDisplayModel,
        newItem: EncounterCreatorDisplayModel
    ): Boolean {
        if (oldItem is EncounterCreatorDisplayModel.Monster && newItem is EncounterCreatorDisplayModel.Monster) {
            return oldItem.id == newItem.id
        }
        if (oldItem is EncounterCreatorDisplayModel.MonsterForEncounter && newItem is EncounterCreatorDisplayModel.MonsterForEncounter) {
            return oldItem.id == newItem.id
        }
        if (oldItem is EncounterCreatorDisplayModel.EncounterInformation && newItem is EncounterCreatorDisplayModel.EncounterInformation) {
            return true
        }
        return false
    }

    override fun areContentsTheSame(
        oldItem: EncounterCreatorDisplayModel,
        newItem: EncounterCreatorDisplayModel
    ): Boolean {
        if (oldItem is EncounterCreatorDisplayModel.Monster && newItem is EncounterCreatorDisplayModel.Monster) {
            return true
        }
        if (oldItem is EncounterCreatorDisplayModel.MonsterForEncounter && newItem is EncounterCreatorDisplayModel.MonsterForEncounter) {
            return oldItem.count == newItem.count
        }
        if (oldItem is EncounterCreatorDisplayModel.EncounterInformation && newItem is EncounterCreatorDisplayModel.EncounterInformation) {
            return oldItem.currentBudget == newItem.currentBudget
        }
        return true
    }


}