package de.enduni.monsterlair.treasure.view

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.liveData
import de.enduni.monsterlair.common.domain.Rarity
import de.enduni.monsterlair.common.domain.TreasureCategory
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
import kotlinx.coroutines.flow.collect
import timber.log.Timber

@ExperimentalCoroutinesApi
class TreasureViewModel(
    private val filterStore: TreasureFilterStore,
    private val treasureRepository: TreasureRepository,
    private val mapper: TreasureDisplayModelMapper
) : ViewModel(), TreasureViewHolder.TreasureViewHolderListener, SortByChip.BottomSheet.Listener,
    SearchChip.BottomSheet.Listener, LevelRangeChip.BottomSheet.Listener {

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

    override fun setLowerLevel(level: Int) {
        filterStore.setLowerLevel(level)
    }

    override fun setUpperLevel(level: Int) {
        filterStore.setUpperLevel(level)
    }

    override fun onSelect(monsterId: String) {
        Timber.d("Do nothing")
    }

    override fun setSearchTerm(searchString: String) {
        filterStore.setSearchTerm(searchString)
    }

    override fun setSortBy(sortBy: SortBy) {
        filterStore.setSortBy(sortBy)
    }

    fun removeTreasureCategory(category: TreasureCategory) {
        filterStore.removeCategory(category)
    }

    fun removeTrait(trait: String) {
        filterStore.removeTrait(trait)
    }

    fun removeRarity(rarity: Rarity) {
        filterStore.removeRarity(rarity)
    }

    fun setLowerGoldCost(cost: Double?) {
        filterStore.setLowerGoldCost(cost)
    }

    fun setUpperGoldCost(cost: Double?) {
        filterStore.setUpperGoldCost(cost)
    }


    fun addTreasureCategory(category: TreasureCategory) {
        filterStore.addCategory(category)
    }

    fun addTrait(trait: String) {
        filterStore.addTrait(trait)
    }

    fun addRarity(rarity: Rarity) {
        filterStore.addRarity(rarity)
    }

}