package de.enduni.monsterlair.treasure.view

import androidx.lifecycle.ViewModel
import androidx.lifecycle.liveData
import de.enduni.monsterlair.treasure.repository.TreasureRepository
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.collect

@ExperimentalCoroutinesApi
class TreasureFilterViewModel(
    val filterStore: TreasureFilterStore,
    private val treasureRepository: TreasureRepository
) : ViewModel() {

    val filter = liveData {
        filterStore.filter.collect { filter -> emit(filter) }
    }

    val traits = liveData {
        emit(treasureRepository.getTraits())
    }

}