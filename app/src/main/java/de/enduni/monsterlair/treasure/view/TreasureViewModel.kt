package de.enduni.monsterlair.treasure.view

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.liveData
import de.enduni.monsterlair.treasure.domain.Treasure
import de.enduni.monsterlair.treasure.domain.TreasureFilter
import de.enduni.monsterlair.treasure.repository.TreasureRepository
import de.enduni.monsterlair.treasure.view.adapter.TreasureViewHolder
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.collect
import timber.log.Timber

@ExperimentalCoroutinesApi
class TreasureViewModel(
    val filterStore: TreasureFilterStore,
    private val treasureRepository: TreasureRepository,
    private val mapper: TreasureDisplayModelMapper
) : ViewModel(), TreasureViewHolder.TreasureViewHolderListener {

    private val handler = CoroutineExceptionHandler { _, exception ->
        Timber.e(exception, "Caught exception")
    }

    val traits = liveData {
        emit(treasureRepository.getTraits())
    }

    val filter = MutableLiveData<TreasureFilter>()

    val treasures = liveData(Dispatchers.IO + handler) {
        filterStore.filter
            .collect { treasureFilter ->
                treasureRepository.getTreasures(treasureFilter)
                    .toDisplayModel()
                    .let { emit(it) }
                filter.postValue(treasureFilter)
            }
    }

    private fun List<Treasure>.toDisplayModel(): List<TreasureDisplayModel> {
        return this.map { mapper.fromDomainToDisplayModel(it) }
    }

    override fun onSelect(monsterId: String) {
        Timber.d("Do nothing")
    }

}