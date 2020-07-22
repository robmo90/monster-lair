package de.enduni.monsterlair.hazards.view

import androidx.lifecycle.ViewModel
import androidx.lifecycle.liveData
import de.enduni.monsterlair.hazards.persistence.HazardRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.collect

@ExperimentalCoroutinesApi
class HazardFilterViewModel(
    val filterStore: HazardFilterStore,
    private val hazardRepository: HazardRepository
) : ViewModel() {

    val filter = liveData {
        filterStore.filter.collect { filter -> emit(filter) }
    }

    val traits = liveData(Dispatchers.IO) {
        emit(hazardRepository.getTraits())
    }

}