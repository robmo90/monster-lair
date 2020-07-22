package de.enduni.monsterlair.treasure.view

import androidx.lifecycle.ViewModel
import androidx.lifecycle.liveData
import de.enduni.monsterlair.common.domain.Rarity
import de.enduni.monsterlair.common.domain.TreasureCategory
import de.enduni.monsterlair.treasure.repository.TreasureRepository
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.collect

@ExperimentalCoroutinesApi
class TreasureFilterDialogViewModel(
    private val filterStore: TreasureFilterStore,
    private val treasureRepository: TreasureRepository
) : ViewModel() {

    val filter = liveData {
        filterStore.filter.collect { filter -> emit(filter) }
    }

    val traits = liveData {
        emit(treasureRepository.getTraits())
    }

    fun addTreasureCategory(category: TreasureCategory) {
        filterStore.addCategory(category)
    }

    fun removeTreasureCategory(category: TreasureCategory) {
        filterStore.removeCategory(category)
    }

    fun addTrait(trait: String) {
        filterStore.addTrait(trait)
    }

    fun removeTrait(trait: String) {
        filterStore.removeTrait(trait)
    }

    fun addRarity(rarity: Rarity) {
        filterStore.addRarity(rarity)
    }

    fun removeRarity(rarity: Rarity) {
        filterStore.removeRarity(rarity)
    }

    fun setLowerGoldCost(value: Double?) {
        filterStore.setLowerGoldCost(value)
    }

    fun setUpperGoldCost(value: Double?) {
        filterStore.setUpperGoldCost(value)
    }

}