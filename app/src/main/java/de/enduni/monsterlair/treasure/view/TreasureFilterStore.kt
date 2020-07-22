package de.enduni.monsterlair.treasure.view

import de.enduni.monsterlair.common.domain.Rarity
import de.enduni.monsterlair.common.domain.TreasureCategory
import de.enduni.monsterlair.common.filter.*
import de.enduni.monsterlair.monsters.view.SortBy
import de.enduni.monsterlair.treasure.domain.TreasureFilter
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlin.math.max
import kotlin.math.min

@ExperimentalCoroutinesApi
class TreasureFilterStore : SearchFilterStore,
    LevelFilterStore, SortByFilterStore,
    RarityFilterStore, TraitFilterStore,
    TreasureCategoryFilterStore,
    GoldCostFilterStore {

    private val _filterFlow = MutableStateFlow(TreasureFilter())
    val filter: Flow<TreasureFilter> get() = _filterFlow

    private var _filter: TreasureFilter
        get() = _filterFlow.value
        set(value) {
            _filterFlow.value = value
        }

    override fun setSearchTerm(searchTerm: String) {
        _filter = _filter.copy(searchTerm = searchTerm)
    }

    override fun setUpperLevel(level: Int) {
        _filter = _filter.copy(upperLevel = level)
    }

    override fun setLowerLevel(level: Int) {
        _filter = _filter.copy(lowerLevel = level)
    }

    override fun setSortBy(sortBy: SortBy) {
        _filter = _filter.copy(sortBy = sortBy)
    }

    override fun addRarity(rarity: Rarity) {
        _filter = _filter.copy(rarities = (_filter.rarities + rarity).distinct())
    }

    override fun removeRarity(rarity: Rarity) {
        _filter = _filter.copy(rarities = _filter.rarities - rarity)
    }

    override fun addTrait(trait: String) {
        _filter = _filter.copy(traits = (_filter.traits + trait).distinct())
    }

    override fun removeTrait(trait: String) {
        _filter = _filter.copy(traits = _filter.traits - trait)
    }

    override fun addCategory(category: TreasureCategory) {
        _filter = _filter.copy(categories = (_filter.categories + category).distinct())
    }

    override fun removeCategory(category: TreasureCategory) {
        _filter = _filter.copy(categories = _filter.categories - category)
    }

    override fun setLowerGoldCost(cost: Double?) {
        _filter = if (cost == null) {
            _filter.copy(lowerGoldCost = cost)
        } else {
            _filter.copy(lowerGoldCost = min(cost, _filter.upperGoldCost ?: 90_000.0))
        }
    }

    override fun setUpperGoldCost(cost: Double?) {
        _filter = if (cost == null) {
            _filter.copy(upperGoldCost = cost)
        } else {
            _filter.copy(upperGoldCost = max(cost, _filter.lowerGoldCost ?: 0.0))
        }
    }
}