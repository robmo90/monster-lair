package de.enduni.monsterlair.treasure.view

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import de.enduni.monsterlair.common.view.filterchips.LevelRangeChip
import de.enduni.monsterlair.common.view.filterchips.SearchChip
import de.enduni.monsterlair.common.view.filterchips.SortByChip
import de.enduni.monsterlair.monsters.view.SortBy
import de.enduni.monsterlair.treasure.domain.Treasure
import de.enduni.monsterlair.treasure.domain.TreasureFilter
import de.enduni.monsterlair.treasure.repository.TreasureRepository
import de.enduni.monsterlair.treasure.view.adapter.TreasureViewHolder
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import timber.log.Timber

@ExperimentalCoroutinesApi
class TreasureViewModel(
    private val treasureRepository: TreasureRepository,
    private val mapper: TreasureDisplayModelMapper
) : ViewModel(), TreasureViewHolder.TreasureViewHolderListener, SortByChip.BottomSheet.Listener,
    SearchChip.BottomSheet.Listener, LevelRangeChip.BottomSheet.Listener {

    private val handler = CoroutineExceptionHandler { _, exception ->
        Timber.e(exception, "Caught exception")
    }

    private val _filter = MutableStateFlow(TreasureFilter())

    val filter = MutableLiveData<TreasureFilter>()

    val treasures = MutableLiveData<List<TreasureDisplayModel>>()

    fun start() {
        viewModelScope.launch(Dispatchers.IO) {
            _filter
                .collect { treasureFilter ->
                    treasureRepository.getTreasures(treasureFilter)
                        .toDisplayModel()
                        .let { treasures.postValue(it) }
                    filter.postValue(treasureFilter)
                }
        }
    }

    private fun List<Treasure>.toDisplayModel(): List<TreasureDisplayModel> {
        return this.map { mapper.fromDomainToDisplayModel(it) }
    }

    override fun adjustLowerLevel(level: Int) {
        _filter.value = _filter.value.copy(lowerLevel = level)
    }

    override fun adjustUpperLevel(level: Int) {
        _filter.value = _filter.value.copy(upperLevel = level)
    }

    override fun onSelect(monsterId: String) {
        Timber.d("Do nothing")
    }

    override fun updateSearch(searchString: String) {
        _filter.value = _filter.value.copy(searchString = searchString)
    }

    override fun updateSortBy(sortBy: SortBy) {
        _filter.value = _filter.value.copy(sortBy = sortBy)
    }

}