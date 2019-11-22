package de.enduni.monsterlair.overview

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.InternalCoroutinesApi
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

@UseExperimental(InternalCoroutinesApi::class)
class MonsterViewModel(
    private val retrieveMonstersUseCase: RetrieveMonstersUseCase,
    private val filterMonstersUseCase: FilterMonstersUseCase
) : ViewModel() {

    private val _viewState = MutableLiveData<MonsterOverviewViewState>()
    val viewState: LiveData<MonsterOverviewViewState> get() = _viewState

    init {
        viewModelScope.launch {
            retrieveMonstersUseCase.execute().collect {
                _viewState.postValue(MonsterOverviewViewState(it))
            }
        }
    }

    fun filterByString(string: String) = viewModelScope.launch {
        val filter = _viewState.value?.filter?.copy(
            string = string
        ) ?: MonsterFilter(string = string)
        filterMonstersUseCase.execute(filter).collect {
            _viewState.postValue(MonsterOverviewViewState(it, filter))
        }
    }

    fun filterByLevel(lowerLevel: Int, higherLevel: Int) = viewModelScope.launch {
        val filter = _viewState.value?.filter?.copy(
            lowerLevel = lowerLevel,
            higherLevel = higherLevel
        ) ?: MonsterFilter(lowerLevel = lowerLevel, higherLevel = higherLevel)
        filterMonstersUseCase.execute(filter).collect {
            _viewState.postValue(MonsterOverviewViewState(it, filter))
        }
    }


}
