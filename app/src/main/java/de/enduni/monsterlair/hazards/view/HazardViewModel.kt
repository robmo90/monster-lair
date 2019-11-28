package de.enduni.monsterlair.hazards.view

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import de.enduni.monsterlair.common.view.ActionLiveData
import de.enduni.monsterlair.hazards.domain.Hazard
import de.enduni.monsterlair.hazards.domain.RetrieveHazardsUseCase
import de.enduni.monsterlair.hazards.view.adapter.HazardViewHolder
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

    private var filter = HazardFilter()

    init {
        viewModelScope.launch {
            filterMonsters()
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

    fun adjustType(hazardType: HazardType) = viewModelScope.launch {
        filterMonsters(filter.copy(type = hazardType))
    }


    private suspend fun filterMonsters(newFilter: HazardFilter) {
        if (newFilter != filter) {
            filter = newFilter
            filterMonsters()
        }
    }

    private suspend fun filterMonsters() {
        Timber.d("Starting monster filter with $filter")
        val hazards = retrieveHazardsUseCase.execute(filter).toDisplayModel()
        val state = HazardOverviewViewState(
            hazards = hazards,
            hazardFilter = filter
        )
        _viewState.postValue(state)
    }

    override fun onSelect(hazardId: Long) {
        viewModelScope.launch {
            val hazard = retrieveHazardsUseCase.getHazard(hazardId)
            _actions.postValue(HazardOverviewAction.HazardSelected(hazard.url))
        }
    }

    fun List<Hazard>.toDisplayModel(): List<HazardDisplayModel> {
        return this.map { mapper.toDisplayModel(it) }
    }

}
