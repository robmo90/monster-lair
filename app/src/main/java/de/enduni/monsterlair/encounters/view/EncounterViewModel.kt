package de.enduni.monsterlair.encounters.view

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import de.enduni.monsterlair.common.view.ActionLiveData
import de.enduni.monsterlair.encounters.domain.CreateEncounterTemplateUseCase
import de.enduni.monsterlair.encounters.domain.DeleteEncounterUseCase
import de.enduni.monsterlair.encounters.domain.RetrieveEncountersUseCase
import de.enduni.monsterlair.encounters.domain.model.Encounter
import de.enduni.monsterlair.encounters.view.adapter.EncounterViewHolder
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import timber.log.Timber

class EncounterViewModel(
    private val retrieveEncountersUseCase: RetrieveEncountersUseCase,
    private val createEncounterTemplateUseCase: CreateEncounterTemplateUseCase,
    private val deleteEncounterUseCase: DeleteEncounterUseCase,
    private val mapper: EncounterDisplayModelMapper
) : ViewModel(), EncounterViewHolder.OnClickListener {

    private val _viewState: MutableLiveData<EncounterState> by lazy {
        fetchEncounters()
        MutableLiveData(EncounterState())
    }
    val viewState: LiveData<EncounterState> get() = _viewState

    private val _actions = ActionLiveData<EncounterAction>()
    val actions: LiveData<EncounterAction> get() = _actions

    private var encounters = listOf<Encounter>()

    private val handler = CoroutineExceptionHandler { _, exception ->
        Timber.e(exception, "Caught exception")
    }

    fun fetchEncounters() {
        viewModelScope.launch(handler) {
            retrieveEncountersUseCase.execute().collect { encounters ->
                this@EncounterViewModel.encounters = encounters
                encounters.map { mapper.mapToDisplayModel(it) }
                    .apply { _viewState.postValue(EncounterState(this)) }
            }
        }
    }

    override fun onEncounterSelected(id: Long) {
        encounters.find { it.id == id }?.let {
            _actions.postValue(
                EncounterAction.EncounterSelectedAction(
                    encounterName = it.name,
                    encounterId = it.id!!,
                    encounterLevel = it.level,
                    numberOfPlayers = it.numberOfPlayers,
                    difficulty = it.targetDifficulty
                )
            )
        }
    }

    override fun onEncounterOptionsSelected(id: Long) {
        encounters.find { it.id == id }?.let {
            _actions.postValue(EncounterAction.EncounterDetailsOpenedAction(it.name, id))
        }

    }

    fun onEncounterDeleted(id: Long) {
        viewModelScope.launch(handler) { deleteEncounterUseCase.execute(id) }
    }

    fun onEncounterExport(id: Long) {
        encounters.find { it.id == id }?.let {
            viewModelScope.launch(handler) {
                val template = createEncounterTemplateUseCase.execute(it)
                _actions.postValue(EncounterAction.ExportEncounterToPdfAction(it.name, template))
            }
        }
    }

}
