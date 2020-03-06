package de.enduni.monsterlair.monsters.view

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import de.enduni.monsterlair.common.domain.MonsterType
import de.enduni.monsterlair.common.view.ActionLiveData
import de.enduni.monsterlair.monsters.domain.Monster
import de.enduni.monsterlair.monsters.domain.RetrieveMonsterUseCase
import de.enduni.monsterlair.monsters.domain.RetrieveMonstersUseCase
import de.enduni.monsterlair.monsters.view.adapter.MonsterViewHolder
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import timber.log.Timber

class MonsterViewModel(
    private val retrieveMonsterUseCase: RetrieveMonsterUseCase,
    private val retrieveMonstersUseCase: RetrieveMonstersUseCase,
    private val mapper: MonsterListDisplayModelMapper
) : ViewModel(), MonsterViewHolder.MonsterViewHolderListener {

    private val _viewState = MutableLiveData<MonsterOverviewViewState>()
    val viewState: LiveData<MonsterOverviewViewState> get() = _viewState

    private val _actions = ActionLiveData<MonsterOverviewAction>()
    val actions: LiveData<MonsterOverviewAction> get() = _actions

    private val handler = CoroutineExceptionHandler { _, exception ->
        Timber.e(exception, "Caught exception")
    }

    private var filter = MonsterFilter()

    fun start() {
        viewModelScope.launch(Dispatchers.Default) {
            filterMonsters()
        }
    }


    fun filterByString(string: String) = viewModelScope.launch(handler) {
        filterMonsters(filter.copy(string = string))
    }

    fun adjustFilterLevelLower(lowerLevel: Int) = viewModelScope.launch(handler) {
        filterMonsters(filter.copy(lowerLevel = lowerLevel))
    }

    fun adjustFilterLevelUpper(upperLevel: Int) = viewModelScope.launch(handler) {
        filterMonsters(filter.copy(upperLevel = upperLevel))
    }

    fun adjustSortBy(sortBy: SortBy) = viewModelScope.launch(handler) {
        filterMonsters(filter.copy(sortBy = sortBy))
    }


    private suspend fun filterMonsters(newFilter: MonsterFilter) {
        if (newFilter != filter) {
            filter = newFilter
            filterMonsters()
        }
    }

    private suspend fun filterMonsters() {
        Timber.d("Starting monster filter with $filter")
        retrieveMonstersUseCase.execute(filter).map { it.toDisplayModel() }
            .collect { monsters ->
                val state = MonsterOverviewViewState(
                    monsters = monsters,
                    filter = filter
                )
                _viewState.postValue(state)
            }
    }


    override fun onSelect(monsterId: Long) {
        viewModelScope.launch(handler) {
            val monster = retrieveMonsterUseCase.execute(monsterId)
            _actions.sendAction(MonsterOverviewAction.MonsterSelected(monster.url))
        }
    }


    private fun List<Monster>.toDisplayModel(): List<MonsterListDisplayModel> {
        return this.map { mapper.toMonsterDisplayModel(it) }
    }

    fun addMonsterTypeFilter(monsterType: MonsterType) = viewModelScope.launch(handler) {
        filterMonsters(filter.copy(monsterTypes = filter.monsterTypes + monsterType))
    }

    fun removeMonsterTypeFilter(monsterType: MonsterType) = viewModelScope.launch(handler) {
        filterMonsters(filter.copy(monsterTypes = filter.monsterTypes - monsterType))
    }

}
