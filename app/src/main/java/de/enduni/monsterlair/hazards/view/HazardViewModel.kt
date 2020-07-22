package de.enduni.monsterlair.hazards.view

import androidx.lifecycle.*
import de.enduni.monsterlair.common.view.ActionLiveData
import de.enduni.monsterlair.hazards.domain.Hazard
import de.enduni.monsterlair.hazards.domain.HazardFilter
import de.enduni.monsterlair.hazards.persistence.HazardRepository
import de.enduni.monsterlair.hazards.view.adapter.HazardViewHolder
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import timber.log.Timber

@ExperimentalCoroutinesApi
class HazardViewModel(
    val filterStore: HazardFilterStore,
    private val mapper: HazardDisplayModelMapper,
    private val hazardRepository: HazardRepository
) : ViewModel(), HazardViewHolder.HazardSelectedListener {

    private val _actions = ActionLiveData<HazardOverviewAction>()
    val actions: LiveData<HazardOverviewAction> get() = _actions

    private val handler = CoroutineExceptionHandler { _, exception ->
        Timber.e(exception, "Caught exception")
    }

    val hazards = liveData(Dispatchers.IO + handler) {
        filterStore.filter
            .collect { hazardFilter ->
                hazardRepository.getFilteredHazards(
                    hazardFilter.searchTerm,
                    hazardFilter.complexities,
                    hazardFilter.rarities,
                    hazardFilter.lowerLevel,
                    hazardFilter.upperLevel,
                    hazardFilter.sortBy.getStringForHazard(),
                    hazardFilter.traits
                )
                    .toDisplayModel()
                    .let { emit(it) }
                filter.postValue(hazardFilter)
            }

    }

    val filter = MutableLiveData<HazardFilter>()

    override fun onSelect(hazardId: String) {
        viewModelScope.launch(handler) {
            val hazard = hazardRepository.getHazard(hazardId)
            _actions.postValue(HazardOverviewAction.HazardSelected(hazard.url))
        }
    }

    fun List<Hazard>.toDisplayModel(): List<HazardDisplayModel> {
        return this.map { mapper.toDisplayModel(it) }
    }

}
