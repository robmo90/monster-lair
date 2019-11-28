package de.enduni.monsterlair.encounters.creator.view

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import de.enduni.monsterlair.common.view.ActionLiveData
import de.enduni.monsterlair.encounters.creator.domain.RetrieveEncounterUseCase
import de.enduni.monsterlair.encounters.creator.domain.RetrieveHazardsWithRoleUseCase
import de.enduni.monsterlair.encounters.creator.domain.RetrieveMonstersWithRoleUseCase
import de.enduni.monsterlair.encounters.creator.domain.StoreEncounterUseCase
import de.enduni.monsterlair.encounters.creator.view.adapter.DangerForEncounterViewHolder
import de.enduni.monsterlair.encounters.creator.view.adapter.DangerViewHolder
import de.enduni.monsterlair.encounters.creator.view.adapter.EncounterBudgetViewHolder
import de.enduni.monsterlair.encounters.domain.CalculateEncounterBudgetUseCase
import de.enduni.monsterlair.encounters.domain.model.*
import de.enduni.monsterlair.monsters.view.SortBy
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import timber.log.Timber

class EncounterCreatorViewModel(
    private val retrieveMonstersWithRoleUseCase: RetrieveMonstersWithRoleUseCase,
    private val retrieveHazardsWithRoleUseCase: RetrieveHazardsWithRoleUseCase,
    private val calculateEncounterBudgetUseCase: CalculateEncounterBudgetUseCase,
    private val retrieveEncounterUseCase: RetrieveEncounterUseCase,
    private val mapper: EncounterCreatorDisplayModelMapper,
    private val storeEncounterUseCase: StoreEncounterUseCase
) : ViewModel(),
    EncounterBudgetViewHolder.OnSaveClickedListener,
    DangerViewHolder.DangerSelectedListener,
    DangerForEncounterViewHolder.DangerForEncounterListener {

    private val _viewState = MutableLiveData<EncounterCreatorDisplayState>()
    val viewState: LiveData<EncounterCreatorDisplayState> get() = _viewState

    private val _actions = ActionLiveData<EncounterCreatorAction>()
    val actions: LiveData<EncounterCreatorAction> get() = _actions

    private var filter = EncounterCreatorFilter()

    private var monsters: List<MonsterWithRole> = listOf()

    private var hazards: List<HazardWithRole> = listOf()

    private lateinit var encounter: Encounter

    fun start(
        numberOfPlayers: Int,
        levelOfEncounter: Int,
        targetDifficulty: EncounterDifficulty,
        encounterId: Long
    ) {
        if (viewState.value != null) {
            return
        }
        if (encounterId == -1L) {
            encounter = Encounter(
                numberOfPlayers = numberOfPlayers,
                level = levelOfEncounter,
                targetDifficulty = targetDifficulty
            )
            val filter = EncounterCreatorFilter(
                lowerLevel = levelOfEncounter - 4,
                upperLevel = levelOfEncounter + 4
            )
            viewModelScope.launch { filterDangers(filter) }
        } else {
            viewModelScope.launch {
                encounter = retrieveEncounterUseCase.execute(encounterId)
                val filter = EncounterCreatorFilter(
                    lowerLevel = levelOfEncounter - 4,
                    upperLevel = levelOfEncounter + 4
                )
                filterDangers(filter)
            }
        }

    }

    fun filterByString(string: String) = viewModelScope.launch {
        filterDangers(filter.copy(string = string))
    }

    fun adjustFilterLevelLower(lowerLevel: Int) = viewModelScope.launch {
        filterDangers(filter.copy(lowerLevel = lowerLevel))
    }

    fun adjustFilterLevelUpper(upperLevel: Int) = viewModelScope.launch {
        filterDangers(filter.copy(upperLevel = upperLevel))
    }

    fun adjustSortBy(sortBy: SortBy) = viewModelScope.launch {
        filterDangers(filter.copy(sortBy = sortBy))
    }


    private suspend fun filterDangers(newFilter: EncounterCreatorFilter) {
        if (newFilter != filter) {
            filter = newFilter
            Timber.d("Starting monster filter with $newFilter")
            monsters = retrieveMonstersWithRoleUseCase.execute(newFilter, encounter.level)
            hazards = retrieveHazardsWithRoleUseCase.execute(newFilter, encounter.level)
            postCurrentState()
        }
    }

    private fun postCurrentState() = viewModelScope.launch {
        launch(Dispatchers.Default) {
            val budget = calculateEncounterBudgetUseCase.execute(encounter)
            val dangers = (monsters.toMonsterDisplayModel() +
                    hazards.toHazardDisplayModel()).sortedBy { it.name }
            val dangersForEncounter = (encounter.monsters.toEncounterMonsterDisplayModel() +
                    encounter.hazards.toEncounterHazardDisplayModel())
                .sortedBy { it.name }

            val list: List<EncounterCreatorDisplayModel> = dangersForEncounter +
                    budget.toBudgetDisplayModel(encounter) +
                    dangers

            val state = EncounterCreatorDisplayState(
                list = list,
                filter = filter
            )
            _viewState.postValue(state)
        }
    }


    private fun List<EncounterMonster>.toEncounterMonsterDisplayModel(): List<EncounterCreatorDisplayModel.DangerForEncounter> {
        return this.map { mapper.toDanger(it) }
    }

    private fun List<EncounterHazard>.toEncounterHazardDisplayModel(): List<EncounterCreatorDisplayModel.DangerForEncounter> {
        return this.map { mapper.toDanger(it) }
    }

    private fun EncounterBudget.toBudgetDisplayModel(encounter: Encounter): List<EncounterCreatorDisplayModel> {
        return listOf(mapper.toBudget(this, encounter.targetDifficulty))
    }

    private fun List<MonsterWithRole>.toMonsterDisplayModel(): List<EncounterCreatorDisplayModel.Danger> {
        return this.map { mapper.toDanger(it) }
    }

    private fun List<HazardWithRole>.toHazardDisplayModel(): List<EncounterCreatorDisplayModel.Danger> {
        return this.map { mapper.toDanger(it) }
    }

    override fun onDecrement(type: DangerType, id: Long) {
        when (type) {
            DangerType.MONSTER -> encounter.decrementCount(monsterId = id)
            DangerType.HAZARD -> encounter.decrementCount(hazardId = id)
        }
        postCurrentState()
    }

    override fun onIncrement(type: DangerType, id: Long) {
        when (type) {
            DangerType.MONSTER -> encounter.incrementCount(monsterId = id)
            DangerType.HAZARD -> encounter.incrementCount(hazardId = id)
        }
        postCurrentState()
    }

    override fun onSelected(type: DangerType, id: Long) {
        when (type) {
            DangerType.MONSTER -> {
                monsters.find { it.id == id }?.let { encounter.addMonster(it) }
            }
            DangerType.HAZARD -> {
                hazards.find { it.id == id }?.let { encounter.addHazard(it) }
            }
        }
        postCurrentState()
    }

    override fun onSaveClicked() {
        _actions.sendAction(EncounterCreatorAction.SaveClicked(encounter.name))
    }


    fun saveEncounter(name: String) {
        viewModelScope.launch {
            encounter.name = name
            storeEncounterUseCase.store(encounter)
            _actions.sendAction(EncounterCreatorAction.EncounterSaved)
        }
    }
}

