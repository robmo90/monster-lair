package de.enduni.monsterlair.encounters.view

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import de.enduni.monsterlair.common.view.ActionLiveData
import de.enduni.monsterlair.encounters.domain.CalculateEncounterBudgetUseCase
import de.enduni.monsterlair.encounters.domain.CreateEncounterTemplateUseCase
import de.enduni.monsterlair.encounters.domain.RetrieveEncountersUseCase
import de.enduni.monsterlair.encounters.domain.model.Encounter
import de.enduni.monsterlair.encounters.domain.model.EncounterDifficulty
import de.enduni.monsterlair.encounters.view.adapter.EncounterViewHolder
import kotlinx.coroutines.launch

class EncounterViewModel(
    private val retrieveEncountersUseCase: RetrieveEncountersUseCase,
    private val calculateEncounterBudgetUseCase: CalculateEncounterBudgetUseCase,
    private val createEncounterTemplateUseCase: CreateEncounterTemplateUseCase,
    private val mapper: EncounterDisplayModelMapper
) : ViewModel(), EncounterViewHolder.OnClickListener {
    private val _viewState = MutableLiveData<EncounterState>()

    val viewState: LiveData<EncounterState> get() = _viewState
    private var encounterState = EncounterState()

    private val _actions = ActionLiveData<EncounterAction>()
    val actions: LiveData<EncounterAction> get() = _actions

    private var encounters = listOf<Encounter>()

    init {
        _viewState.postValue(encounterState)
    }

    fun start() {
        viewModelScope.launch {
            encounters = retrieveEncountersUseCase.execute()
            val encounterDisplayModels = encounters.map {
                Pair(it, calculateEncounterBudgetUseCase.execute(it))
            }.map { (encounter, budget) ->
                mapper.mapToDisplayModel(encounter, budget)
            }
            postNewStateIfDifferent(
                encounterState.copy(
                    encounters = encounterDisplayModels
                )
            )
        }
    }

    fun setLevel(levelString: String) {
        val level = levelString.toIntOrNull()
        val viewState = encounterState.copy(
            levelOfPlayers = level,
            levelValid = IntRange(0, 20).contains(level)
        )
        postNewStateIfDifferent(viewState)
    }


    fun setNumber(numberString: String) {
        val number = numberString.toIntOrNull()
        val viewState = encounterState.copy(
            numberOfPlayers = number,
            numberValid = IntRange(0, 20).contains(number)
        )
        postNewStateIfDifferent(viewState)
    }

    fun setDifficulty(difficulty: EncounterDifficulty) {
        val viewState = encounterState.copy(
            difficulty = difficulty
        )
        postNewStateIfDifferent(viewState)
    }

    private fun postNewStateIfDifferent(newState: EncounterState) {
        if (encounterState != newState) {
            encounterState = newState
            _viewState.postValue(newState)
        }
    }

    override fun onEncounterSelected(id: Long) {
        encounters.find { it.id == id }?.let {
            _actions.postValue(
                EncounterAction.EncounterSelectedAction(
                    encounterId = it.id!!,
                    encounterLevel = it.level,
                    numberOfPlayers = it.numberOfPlayers,
                    difficulty = it.targetDifficulty
                )
            )
        }
    }

    override fun onEncounterExport(id: Long) {
        encounters.find { it.id == id }?.let {
            viewModelScope.launch {
                val template = createEncounterTemplateUseCase.execute(it)
                _actions.postValue(EncounterAction.ExportEncounterToPdfAction(it.name, template))
            }
        }
    }

}
