package de.enduni.monsterlair.hazards.view

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import de.enduni.monsterlair.common.domain.Complexity
import de.enduni.monsterlair.common.view.ActionLiveData
import de.enduni.monsterlair.hazards.domain.Hazard
import de.enduni.monsterlair.hazards.domain.RetrieveHazardsUseCase
import de.enduni.monsterlair.hazards.view.adapter.HazardViewHolder
import de.enduni.monsterlair.monsters.view.SortBy
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import timber.log.Timber

class HazardViewModel(
    private val mapper: HazardDisplayModelMapper,
    private val retrieveHazardsUseCase: RetrieveHazardsUseCase
) : ViewModel(), HazardViewHolder.HazardSelectedListener {

    private val _viewState = MutableLiveData<HazardOverviewViewState>()
    val viewState: LiveData<HazardOverviewViewState> get() = _viewState

    private val _actions = ActionLiveData<HazardOverviewAction>()
    val actions: LiveData<HazardOverviewAction> get() = _actions

    private val handler = CoroutineExceptionHandler { _, exception ->
        Timber.e(exception, "Caught exception")
    }

    private var filter = HazardFilter()

    init {
        viewModelScope.launch {
            filterHazards()
        }
    }


    fun filterByString(string: String) = viewModelScope.launch(handler) {
        filterHazards(filter.copy(string = string))
    }

    fun adjustFilterLevelLower(lowerLevel: Int) = viewModelScope.launch(handler) {
        filterHazards(filter.copy(lowerLevel = lowerLevel))
    }

    fun adjustFilterLevelUpper(upperLevel: Int) = viewModelScope.launch(handler) {
        filterHazards(filter.copy(upperLevel = upperLevel))
    }

    fun addComplexityFilter(complexity: Complexity) = viewModelScope.launch(handler) {
        filterHazards(filter.copy(complexities = filter.complexities + complexity))
    }

    fun removeComplexityFilter(complexity: Complexity) = viewModelScope.launch(handler) {
        filterHazards(filter.copy(complexities = filter.complexities - complexity))
    }

    fun adjustSortBy(sortBy: SortBy) = viewModelScope.launch(handler) {
        filterHazards(filter.copy(sortBy = sortBy))
    }

    private suspend fun filterHazards(newFilter: HazardFilter) {
        if (newFilter != filter) {
            filter = newFilter
            filterHazards()
        }
    }

    private suspend fun filterHazards() {
        Timber.d("Starting monster filter with $filter")
        retrieveHazardsUseCase.execute(filter).map { it.toDisplayModel() }.collect { hazards ->
            val state = HazardOverviewViewState(
                hazards = hazards,
                hazardFilter = filter
            )
            _viewState.postValue(state)
        }

    }

    override fun onSelect(hazardId: String) {
        viewModelScope.launch(handler) {
            val hazard = retrieveHazardsUseCase.getHazard(hazardId)
            _actions.postValue(HazardOverviewAction.HazardSelected(hazard.url))
        }
    }

    fun List<Hazard>.toDisplayModel(): List<HazardDisplayModel> {
        return this.map { mapper.toDisplayModel(it) }
    }

}
