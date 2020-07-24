package de.enduni.monsterlair.creator.view.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import de.enduni.monsterlair.R
import de.enduni.monsterlair.creator.view.AdjustMonsterStrengthDialog
import de.enduni.monsterlair.creator.view.EncounterCreatorDisplayModel

class EncounterCreatorListAdapter(
    private val layoutInflater: LayoutInflater,
    private val dangerSelectedListener: DangerViewHolder.DangerSelectedListener,
    private val dangerForEncounterListener: DangerForEncounterViewHolder.DangerForEncounterListener,
    private val onSaveClickedListener: EncounterDetailViewHolder.ClickListener,
    private val adjustMonsterStrengthListener: AdjustMonsterStrengthDialog.Listener
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
                DangerForEncounterViewHolder(
                    view,
                    dangerForEncounterListener,
                    adjustMonsterStrengthListener
                )
            }
            TYPE_ENCOUNTER_DETAIL -> {
                val view =
                    layoutInflater.inflate(R.layout.viewholder_encounter_budget, parent, false)
                EncounterDetailViewHolder(view, onSaveClickedListener)
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
            is EncounterCreatorDisplayModel.EncounterDetails -> TYPE_ENCOUNTER_DETAIL
        }
    }


    companion object {
        const val TYPE_DANGER = -2
        const val TYPE_DANGER_FOR_ENCOUNTER = -1
        const val TYPE_ENCOUNTER_DETAIL = 2
    }
}

class EncounterCreatorDiffCallback : DiffUtil.ItemCallback<EncounterCreatorDisplayModel>() {

    override fun areItemsTheSame(
        oldItem: EncounterCreatorDisplayModel,
        newItem: EncounterCreatorDisplayModel
    ): Boolean {
        if (oldItem is EncounterCreatorDisplayModel.Danger && newItem is EncounterCreatorDisplayModel.Danger) {
            return oldItem.id == newItem.id
        }
        if (oldItem is EncounterCreatorDisplayModel.DangerForEncounter && newItem is EncounterCreatorDisplayModel.DangerForEncounter) {
            return oldItem.id == newItem.id && oldItem.strength == newItem.strength
        }
        if (oldItem is EncounterCreatorDisplayModel.EncounterDetails && newItem is EncounterCreatorDisplayModel.EncounterDetails) {
            return true
        }
        return false
    }

    override fun areContentsTheSame(
        oldItem: EncounterCreatorDisplayModel,
        newItem: EncounterCreatorDisplayModel
    ): Boolean {
        if (oldItem is EncounterCreatorDisplayModel.EncounterDetails && newItem is EncounterCreatorDisplayModel.EncounterDetails) {
            return oldItem == newItem
        }
        if (oldItem is EncounterCreatorDisplayModel.Danger && newItem is EncounterCreatorDisplayModel.Danger) {
            return true
        }
        if (oldItem is EncounterCreatorDisplayModel.DangerForEncounter && newItem is EncounterCreatorDisplayModel.DangerForEncounter) {
            return oldItem == newItem
        }
        return true
    }


}