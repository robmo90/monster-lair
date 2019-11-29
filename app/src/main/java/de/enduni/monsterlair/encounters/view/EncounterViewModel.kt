package de.enduni.monsterlair.encounters.view

import android.net.Uri
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import de.enduni.monsterlair.common.view.ActionLiveData
import de.enduni.monsterlair.encounters.creator.domain.RetrieveEncounterUseCase
import de.enduni.monsterlair.encounters.domain.CalculateEncounterBudgetUseCase
import de.enduni.monsterlair.encounters.domain.CreateEncounterTemplateUseCase
import de.enduni.monsterlair.encounters.domain.RetrieveEncountersUseCase
import de.enduni.monsterlair.encounters.domain.model.Encounter
import de.enduni.monsterlair.encounters.domain.model.EncounterDifficulty
import de.enduni.monsterlair.encounters.view.adapter.EncounterViewHolder
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.launch
import timber.log.Timber

class EncounterViewModel(
    private val retrieveEncountersUseCase: RetrieveEncountersUseCase,
    private val retrieveEncounterUseCase: RetrieveEncounterUseCase,
    private val calculateEncounterBudgetUseCase: CalculateEncounterBudgetUseCase,
    private val createEncounterTemplateUseCase: CreateEncounterTemplateUseCase
) : ViewModel(), EncounterViewHolder.OnClickListener {
    private val _viewState = MutableLiveData<EncounterState>()

    val viewState: LiveData<EncounterState> get() = _viewState
    private var encounterState = EncounterState()

    private val _actions = ActionLiveData<EncounterAction>()
    val actions: LiveData<EncounterAction> get() = _actions

    private var encounters = listOf<Encounter>()

    private var exportedEncounterId: Long? = null

    init {
        _viewState.postValue(encounterState)
    }

    fun start() {
        viewModelScope.launch {
            encounters = retrieveEncountersUseCase.execute()
            val encounterDisplayModels = encounters.map {
                Pair(it, calculateEncounterBudgetUseCase.execute(it))
            }.map { (encounter, budget) ->
                EncounterDisplayModel(
                    encounter.id!!,
                    encounter.name,
                    budget.currentBudget,
                    encounter.monsters.joinToString { encounterMonster ->
                        "${encounterMonster.count} ${encounterMonster.monster.name}"
                    } + ", " + encounter.hazards.joinToString { encounterHazard ->
                        "${encounterHazard.count} ${encounterHazard.hazard.name}"
                    }
                )
            }
            _viewState.postValue(
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

    fun exportEncounter(uri: Uri) {
        exportedEncounterId?.let { id ->
            val exceptionHandler = CoroutineExceptionHandler { _, throwable -> Timber.e(throwable) }
            viewModelScope.launch(exceptionHandler) {
                val encounter = retrieveEncounterUseCase.execute(id)

            }
        }
    }
}
