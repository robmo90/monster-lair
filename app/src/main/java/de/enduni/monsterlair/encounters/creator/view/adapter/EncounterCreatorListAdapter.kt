package de.enduni.monsterlair.encounters.creator.view.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import de.enduni.monsterlair.R
import de.enduni.monsterlair.encounters.creator.view.EncounterCreatorDisplayModel

class EncounterCreatorListAdapter(
    private val layoutInflater: LayoutInflater,
    private val dangerSelectedListener: DangerViewHolder.DangerSelectedListener,
    private val dangerForEncounterListener: DangerForEncounterViewHolder.DangerForEncounterListener,
    private val onSaveClickedListener: EncounterBudgetViewHolder.OnSaveClickedListener,
    private val onNameChangedListener: EncounterDetailViewHolder.OnNameChangedListener

) : ListAdapter<EncounterCreatorDisplayModel, EncounterCreatorViewHolder>(
    EncounterCreatorDiffCallback()
) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): EncounterCreatorViewHolder {
        return when (viewType) {
            TYPE_DANGER -> {
                val view = layoutInflater.inflate(R.layout.viewholder_danger, parent, false)
                DangerViewHolder(view, dangerSelectedListener)
            }
            TYPE_DANGER_FOR_ENCOUNTER -> {
                val view =
                    layoutInflater.inflate(R.layout.viewholder_encounter_monster, parent, false)
                DangerForEncounterViewHolder(view, dangerForEncounterListener)
            }
            TYPE_ENCOUNTER_BUDGET -> {
                val view =
                    layoutInflater.inflate(R.layout.viewholder_encounter_budget, parent, false)
                EncounterBudgetViewHolder(view, onSaveClickedListener)
            }
            TYPE_ENCOUNTER_DETAILS -> {
                val view =
                    layoutInflater.inflate(R.layout.viewholder_encounter_detail, parent, false)
                EncounterDetailViewHolder(view, onNameChangedListener)
            }
            else -> throw RuntimeException("Something went wrong")
        }

    }

    override fun onBindViewHolder(holder: EncounterCreatorViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    override fun getItemViewType(position: Int): Int {
        return when (getItem(position)) {
            is EncounterCreatorDisplayModel.Danger -> TYPE_DANGER
            is EncounterCreatorDisplayModel.DangerForEncounter -> TYPE_DANGER_FOR_ENCOUNTER
            is EncounterCreatorDisplayModel.EncounterBudget -> TYPE_ENCOUNTER_BUDGET
            is EncounterCreatorDisplayModel.EncounterDetail -> TYPE_ENCOUNTER_DETAILS
        }
    }


    companion object {
        const val TYPE_DANGER = -2
        const val TYPE_DANGER_FOR_ENCOUNTER = -1
        const val TYPE_ENCOUNTER_BUDGET = 2
        const val TYPE_ENCOUNTER_DETAILS = 0
    }
}

class EncounterCreatorDiffCallback : DiffUtil.ItemCallback<EncounterCreatorDisplayModel>() {

    override fun areItemsTheSame(
        oldItem: EncounterCreatorDisplayModel,
        newItem: EncounterCreatorDisplayModel
    ): Boolean {
        if (oldItem is EncounterCreatorDisplayModel.EncounterDetail && newItem is EncounterCreatorDisplayModel.EncounterDetail) {
            return true
        }
        if (oldItem is EncounterCreatorDisplayModel.Danger && newItem is EncounterCreatorDisplayModel.Danger) {
            return oldItem.id == newItem.id
        }
        if (oldItem is EncounterCreatorDisplayModel.DangerForEncounter && newItem is EncounterCreatorDisplayModel.DangerForEncounter) {
            return oldItem.id == newItem.id
        }
        if (oldItem is EncounterCreatorDisplayModel.EncounterBudget && newItem is EncounterCreatorDisplayModel.EncounterBudget) {
            return true
        }
        return false
    }

    override fun areContentsTheSame(
        oldItem: EncounterCreatorDisplayModel,
        newItem: EncounterCreatorDisplayModel
    ): Boolean {
        if (oldItem is EncounterCreatorDisplayModel.EncounterBudget && newItem is EncounterCreatorDisplayModel.EncounterBudget) {
            return oldItem.currentBudget == newItem.currentBudget
        }
        if (oldItem is EncounterCreatorDisplayModel.EncounterDetail && newItem is EncounterCreatorDisplayModel.EncounterDetail) {
            return oldItem.name == newItem.name
        }
        if (oldItem is EncounterCreatorDisplayModel.Danger && newItem is EncounterCreatorDisplayModel.Danger) {
            return true
        }
        if (oldItem is EncounterCreatorDisplayModel.DangerForEncounter && newItem is EncounterCreatorDisplayModel.DangerForEncounter) {
            return oldItem.count == newItem.count
        }
        return true
    }


}