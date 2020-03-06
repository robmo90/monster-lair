package de.enduni.monsterlair.creator.view

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import de.enduni.monsterlair.common.domain.Complexity
import de.enduni.monsterlair.common.domain.MonsterType
import de.enduni.monsterlair.common.getDefaultMaxLevel
import de.enduni.monsterlair.common.view.ActionLiveData
import de.enduni.monsterlair.creator.domain.*
import de.enduni.monsterlair.creator.view.adapter.DangerForEncounterViewHolder
import de.enduni.monsterlair.creator.view.adapter.DangerViewHolder
import de.enduni.monsterlair.creator.view.adapter.EncounterDetailViewHolder
import de.enduni.monsterlair.encounters.domain.model.*
import de.enduni.monsterlair.monsters.view.SortBy
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import timber.log.Timber

class EncounterCreatorViewModel(
    private val retrieveMonstersWithRoleUseCase: RetrieveMonstersWithRoleUseCase,
    private val retrieveHazardsWithRoleUseCase: RetrieveHazardsWithRoleUseCase,
    private val retrieveEncounterUseCase: RetrieveEncounterUseCase,
    private val createRandomEncounterUseCase: CreateRandomEncounterUseCase,
    private val mapper: EncounterCreatorDisplayModelMapper,
    private val storeEncounterUseCase: StoreEncounterUseCase
) : ViewModel(),
    EncounterDetailViewHolder.ClickListener,
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

    private val handler = CoroutineExceptionHandler { _, exception ->
        Timber.e(exception, "Caught exception")
    }

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
                upperLevel = levelOfEncounter + targetDifficulty.getDefaultMaxLevel()
            )
            viewModelScope.launch(handler) { filterDangers(filter) }
        } else {
            viewModelScope.launch(handler) {
                encounter = retrieveEncounterUseCase.execute(encounterId)
                val filter = EncounterCreatorFilter(
                    lowerLevel = levelOfEncounter - 4,
                    upperLevel = levelOfEncounter + 4
                )
                filterDangers(filter)
            }
        }

    }

    fun filterByString(string: String) = viewModelScope.launch(handler) {
        filterDangers(filter.copy(string = string))
    }

    fun adjustFilterLevelLower(lowerLevel: Int) = viewModelScope.launch(handler) {
        filterDangers(filter.copy(lowerLevel = lowerLevel))
    }

    fun adjustFilterLevelUpper(upperLevel: Int) = viewModelScope.launch(handler) {
        filterDangers(filter.copy(upperLevel = upperLevel))
    }

    fun adjustFilterWithinBudget(withinBudget: Boolean) = viewModelScope.launch(handler) {
        filterDangers(filter.copy(withinBudget = withinBudget))
    }

    fun addMonsterTypeFilter(monsterType: MonsterType) = viewModelScope.launch(handler) {
        filterDangers(filter.copy(monsterTypes = filter.monsterTypes + monsterType))
    }

    fun removeMonsterTypeFilter(monsterType: MonsterType) = viewModelScope.launch(handler) {
        filterDangers(filter.copy(monsterTypes = filter.monsterTypes - monsterType))
    }

    fun addComplexityFilter(complexity: Complexity) = viewModelScope.launch(handler) {
        filterDangers(filter.copy(complexities = filter.complexities + complexity))
    }

    fun removeComplexityFilter(complexity: Complexity) = viewModelScope.launch(handler) {
        filterDangers(filter.copy(complexities = filter.complexities - complexity))
    }

    fun addDangerFilter(dangerType: DangerType) = viewModelScope.launch(handler) {
        filterDangers(filter.copy(dangerTypes = filter.dangerTypes + dangerType))
    }

    fun removeDangerFilter(dangerType: DangerType) = viewModelScope.launch(handler) {
        filterDangers(filter.copy(dangerTypes = filter.dangerTypes - dangerType))
    }

    fun adjustSortBy(sortBy: SortBy) = viewModelScope.launch(handler) {
        filterDangers(filter.copy(sortBy = sortBy))
    }


    private suspend fun filterDangers(newFilter: EncounterCreatorFilter) {
        if (newFilter != filter) {
            filter = newFilter
            Timber.d("Starting monster filter with $newFilter")
            monsters = retrieveMonstersWithRoleUseCase.execute(newFilter, encounter)
            hazards = retrieveHazardsWithRoleUseCase.execute(newFilter, encounter)
            postCurrentState()
        }
    }

    private suspend fun encounterChanged() {
        if (filter.withinBudget) {
            monsters = retrieveMonstersWithRoleUseCase.execute(filter, encounter)
            hazards = retrieveHazardsWithRoleUseCase.execute(filter, encounter)
        }
        postCurrentState()
    }

    private fun postCurrentState() = viewModelScope.launch(handler) {
        launch(Dispatchers.Default) {
            val dangers = (monsters.toMonsterDisplayModel() +
                    hazards.toHazardDisplayModel())
                .sort()
            val dangersForEncounter = (encounter.monsters.toEncounterMonsterDisplayModel() +
                    encounter.hazards.toEncounterHazardDisplayModel())
                .sortedBy { it.name }

            val list: List<EncounterCreatorDisplayModel> =
//                encounter.toDetailDisplayModel() +
                        dangersForEncounter +
                        encounter.toBudgetDisplayModel() +
                        dangers

            val state = EncounterCreatorDisplayState(
                encounterName = encounter.name,
                list = list,
                filter = filter
            )
            _viewState.postValue(state)
        }
    }

    private fun List<EncounterCreatorDisplayModel.Danger>.sort(): List<EncounterCreatorDisplayModel.Danger> {
        return when (filter.sortBy) {
            SortBy.NAME -> this.sortedBy { it.name }
            SortBy.LEVEL -> this.sortedBy { it.level }
            SortBy.TYPE -> this.sortedBy { it.originalType }
        }
    }

    private fun List<EncounterMonster>.toEncounterMonsterDisplayModel(): List<EncounterCreatorDisplayModel.DangerForEncounter> {
        return this.map { mapper.toDanger(it) }
    }

    private fun List<EncounterHazard>.toEncounterHazardDisplayModel(): List<EncounterCreatorDisplayModel.DangerForEncounter> {
        return this.map { mapper.toDanger(it) }
    }

    private fun Encounter.toBudgetDisplayModel(): List<EncounterCreatorDisplayModel> {
        return listOf(mapper.toBudget(this))
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
        viewModelScope.launch(handler) {
            encounterChanged()
        }
    }

    override fun onIncrement(type: DangerType, id: Long) {
        when (type) {
            DangerType.MONSTER -> encounter.incrementCount(monsterId = id)
            DangerType.HAZARD -> encounter.incrementCount(hazardId = id)
        }
        viewModelScope.launch(handler) {
            encounterChanged()
        }

    }

    override fun onDangerForEncounterSelected(url: String) {
        linkClicked(url)
    }

    override fun onDangerSelected(url: String) {
        linkClicked(url)
    }

    private fun linkClicked(url: String) {
        viewModelScope.launch(handler) {
            _actions.sendAction(EncounterCreatorAction.DangerLinkClicked(url))
        }
    }

    override fun onRandomClicked() {
        viewModelScope.launch(handler) {
            withContext(Dispatchers.Default) {
                val filter = filter.copy(withinBudget = false)
                val monsters = retrieveMonstersWithRoleUseCase.execute(filter, encounter)
                val hazards = retrieveHazardsWithRoleUseCase.execute(filter, encounter)
                createRandomEncounterUseCase.createRandomEncounter(encounter, monsters, hazards)
                postCurrentState()
            }
        }
    }

    override fun onSaveClicked() {
        viewModelScope.launch(handler) {
            encounter.name = if (encounter.name.isBlank()) "Random Encounter" else encounter.name
            storeEncounterUseCase.store(encounter)
            _actions.sendAction(EncounterCreatorAction.EncounterSaved)
        }
    }

    override fun onAddClicked(type: DangerType, id: Long) {
        viewModelScope.launch(handler) {
            when (type) {
                DangerType.MONSTER -> {
                    findMonsterWithId(id)?.let {
                        encounter.addMonster(it)
                        _actions.postValue(EncounterCreatorAction.DangerAdded(it.name))
                    }
                }
                DangerType.HAZARD -> {
                    findHazardWithId(id)?.let {
                        encounter.addHazard(it)
                        _actions.postValue(EncounterCreatorAction.DangerAdded(it.name))
                    }
                }
            }
            encounterChanged()
        }
    }

    private suspend fun findMonsterWithId(id: Long): MonsterWithRole? =
        withContext(Dispatchers.Default) {
            monsters.find { it.id == id }
        }

    private suspend fun findHazardWithId(id: Long): HazardWithRole? =
        withContext(Dispatchers.Default) {
            hazards.find { it.id == id }
        }

    fun adjustEncounter(
        encounterName: String,
        numberOfPlayers: Int,
        encounterLevel: Int,
        encounterDifficulty: EncounterDifficulty
    ) {
        encounter.name = encounterName
        encounter.numberOfPlayers = numberOfPlayers
        encounter.level = encounterLevel
        encounter.targetDifficulty = encounterDifficulty
        postCurrentState()
    }

    fun onEditClicked() {
        viewModelScope.launch(handler) {
            _actions.postValue(EncounterCreatorAction.EditEncounterClicked(encounter))
        }
    }


}

