package de.enduni.monsterlair.monsters.view

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import de.enduni.monsterlair.monsters.domain.Monster
import de.enduni.monsterlair.monsters.domain.RetrieveMonstersUseCase
import de.enduni.monsterlair.monsters.view.adapter.MonsterViewHolder
import kotlinx.coroutines.launch
import timber.log.Timber

class MonsterViewModel(
    private val retrieveMonstersUseCase: RetrieveMonstersUseCase,
    private val mapper: MonsterListDisplayModelMapper
) : ViewModel(), MonsterViewHolder.MonsterViewHolderListener {

    private val _viewState = MutableLiveData<MonsterOverviewViewState>()
    val viewState: LiveData<MonsterOverviewViewState> get() = _viewState

    private var filter = MonsterFilter()

    init {
        viewModelScope.launch { filterMonsters() }
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


    private suspend fun filterMonsters(newFilter: MonsterFilter) {
        if (newFilter != filter) {
            filter = newFilter
            filterMonsters()
        }
    }

    private suspend fun filterMonsters() {
        Timber.d("Starting monster filter with $filter")
        val monsters = retrieveMonstersUseCase.execute(filter).toDisplayModel()
        val state = MonsterOverviewViewState(
            monsters = monsters,
            filter = filter
        )
        _viewState.postValue(state)
    }


    override fun onSelect(monsterName: String) {
        Timber.d("Selected $monsterName")
    }


    private fun List<Monster>.toDisplayModel(): List<MonsterListDisplayModel> {
        return this.map { mapper.toMonsterDisplayModel(it) }
    }

}
