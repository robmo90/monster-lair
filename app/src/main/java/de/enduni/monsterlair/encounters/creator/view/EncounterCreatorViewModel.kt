package de.enduni.monsterlair.encounters.creator.view

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import de.enduni.monsterlair.common.view.ActionLiveData
import de.enduni.monsterlair.encounters.creator.domain.RetrieveEncounterUseCase
import de.enduni.monsterlair.encounters.creator.domain.RetrieveMonstersWithRoleUseCase
import de.enduni.monsterlair.encounters.creator.view.adapter.EncounterBudgetViewHolder
import de.enduni.monsterlair.encounters.creator.view.adapter.MonsterForEncounterViewHolder
import de.enduni.monsterlair.encounters.creator.view.adapter.MonsterViewHolder
import de.enduni.monsterlair.encounters.domain.CalculateEncounterBudgetUseCase
import de.enduni.monsterlair.encounters.domain.model.*
import de.enduni.monsterlair.encounters.persistence.EncounterRepository
import de.enduni.monsterlair.monsters.view.SortBy
import kotlinx.coroutines.launch
import timber.log.Timber

class EncounterCreatorViewModel(
    private val retrieveMonstersWithRoleUseCase: RetrieveMonstersWithRoleUseCase,
    private val calculateEncounterBudgetUseCase: CalculateEncounterBudgetUseCase,
    private val retrieveEncounterUseCase: RetrieveEncounterUseCase,
    private val mapper: EncounterCreatorDisplayModelMapper,
    private val encounterRepository: EncounterRepository
) : ViewModel(), MonsterViewHolder.MonsterViewHolderListener,
    MonsterForEncounterViewHolder.MonsterForEncounterListener,
    EncounterBudgetViewHolder.OnSaveClickedListener {

    private val _viewState = MutableLiveData<EncounterCreatorDisplayState>()
    val viewState: LiveData<EncounterCreatorDisplayState> get() = _viewState

    private val _actions = ActionLiveData<EncounterCreatorAction>()
    val actions: LiveData<EncounterCreatorAction> get() = _actions

    private var filter = EncounterCreatorFilter()

    private var monsters: List<MonsterWithRole> = listOf()

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
            viewModelScope.launch { filterMonsters(filter) }
        } else {
            viewModelScope.launch {
                encounter = retrieveEncounterUseCase.execute(encounterId)
                val filter = EncounterCreatorFilter(
                    lowerLevel = levelOfEncounter - 4,
                    upperLevel = levelOfEncounter + 4
                )
                filterMonsters(filter)
            }
        }

    }

    fun filterByString(string: String) = viewModelScope.launch {
        filterMonsters(filter.copy(string = string))
    }

    fun adjustFilterLevelLower(lowerLevel: Int) = viewModelScope.launch {
        filterMonsters(filter.copy(lowerLevel = lowerLevel))
    }

    fun adjustFilterLevelUpper(upperLevel: Int) = viewModelScope.launch {
        filterMonsters(filter.copy(upperLevel = upperLevel))
    }

    fun adjustSortBy(sortBy: SortBy) = viewModelScope.launch {
        filterMonsters(filter.copy(sortBy = sortBy))
    }


    private suspend fun filterMonsters(newFilter: EncounterCreatorFilter) {
        if (newFilter != filter) {
            filter = newFilter
            Timber.d("Starting monster filter with $newFilter")
            monsters = retrieveMonstersWithRoleUseCase.execute(newFilter, encounter.level)
            postCurrentState()
        }
    }

    private fun postCurrentState() = viewModelScope.launch {
        val budget = calculateEncounterBudgetUseCase.execute(encounter)
        val list = encounter.monsters.toEncounterMonsterDisplayModel() +
                budget.toBudgetDisplayModel(encounter) +
                monsters.toMonsterDisplayModel()
        val state = EncounterCreatorDisplayState(
            list = list,
            filter = filter
        )
        _viewState.postValue(state)
    }


    private fun List<EncounterMonster>.toEncounterMonsterDisplayModel(): List<EncounterCreatorDisplayModel.MonsterForEncounter> {
        return this.map { mapper.toMonsterForEncounter(it) }
    }

    private fun EncounterBudget.toBudgetDisplayModel(encounter: Encounter): List<EncounterCreatorDisplayModel.EncounterInformation> {
        return listOf(mapper.toBudget(this, encounter.targetDifficulty))
    }

    private fun List<MonsterWithRole>.toMonsterDisplayModel(): List<EncounterCreatorDisplayModel.Monster> {
        return this.map { mapper.toMonster(it) }
    }

    override fun onSelect(id: Long) {
        monsters.find { it.id == id }
            ?.let { encounter.addMonster(it) }
        postCurrentState()
    }

    override fun onIncrement(monsterId: Long) {
        encounter.incrementCount(monsterId)
        postCurrentState()
    }

    override fun onDecrement(monsterId: Long) {
        encounter.decrementCount(monsterId)
        postCurrentState()
    }

    override fun onSaveClicked() {
        _actions.sendAction(EncounterCreatorAction.SaveClicked)
    }

    fun saveEncounter(name: String) {
        viewModelScope.launch {
            encounter.name = name
            encounterRepository.saveEncounter(
                encounter
            )

        }
    }
}

