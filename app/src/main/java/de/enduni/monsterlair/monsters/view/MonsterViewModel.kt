package de.enduni.monsterlair.monsters.view

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import de.enduni.monsterlair.monsters.domain.Monster
import de.enduni.monsterlair.monsters.domain.RetrieveMonstersUseCase
import kotlinx.coroutines.InternalCoroutinesApi
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

@UseExperimental(InternalCoroutinesApi::class)
class MonsterViewModel(
    private val retrieveMonstersUseCase: RetrieveMonstersUseCase,
    private val mapper: MonsterListDisplayModelMapper
) : ViewModel() {

    private val _viewState = MutableLiveData<MonsterOverviewViewState>()
    val viewState: LiveData<MonsterOverviewViewState> get() = _viewState

    init {
        viewModelScope.launch {
            filterMonsters(MonsterFilter())
        }
    }

    fun filterByString(string: String) = viewModelScope.launch {
        val filter = _viewState.value?.filter?.copy(
            string = string
        ) ?: MonsterFilter(string = string)
        filterMonsters(filter)
    }

    fun adjustFilterLevelLower(lowerLevel: Int) = viewModelScope.launch {
        val filter = _viewState.value?.filter?.copy(
            lowerLevel = lowerLevel
        ) ?: MonsterFilter(lowerLevel = lowerLevel)
        filterMonsters(filter)
    }

    fun adjustFilterLevelUpper(upperLevel: Int) = viewModelScope.launch {
        val filter = _viewState.value?.filter?.copy(
            upperLevel = upperLevel
        ) ?: MonsterFilter(upperLevel = upperLevel)
        filterMonsters(filter)
    }

    fun adjustSortBy(sortBy: SortBy) = viewModelScope.launch {
        val filter = _viewState.value?.filter?.copy(
            sortBy = sortBy
        ) ?: MonsterFilter(sortBy = sortBy)
        filterMonsters(filter)
    }


    private suspend fun filterMonsters(filter: MonsterFilter) {
        if (filter == _viewState.value?.filter) {
            return
        }
        retrieveMonstersUseCase.execute(filter).collect {
            _viewState.postValue(
                MonsterOverviewViewState(
                    it.toListDisplayModel(),
                    filter
                )
            )
        }
    }

    private fun List<Monster>.toListDisplayModel(): List<MonsterListDisplayModel> {
        return this.map { mapper.fromDomain(it) }
    }


}
