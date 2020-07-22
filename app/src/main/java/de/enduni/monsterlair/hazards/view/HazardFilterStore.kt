package de.enduni.monsterlair.hazards.view

import de.enduni.monsterlair.common.domain.*
import de.enduni.monsterlair.common.filter.*
import de.enduni.monsterlair.hazards.domain.HazardFilter
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow

@ExperimentalCoroutinesApi
class HazardFilterStore : LevelFilterStore, RarityFilterStore, SearchFilterStore, SortByFilterStore,
    TraitFilterStore, ComplexityFilterStore {

    private val _filterFlow = MutableStateFlow(HazardFilter())
    val filter: Flow<HazardFilter> get() = _filterFlow

    private var _filter: HazardFilter
        get() = _filterFlow.value
        set(value) {
            _filterFlow.value = value
        }

    override fun setSearchTerm(searchTerm: String) {
        _filter = _filter.copy(searchTerm = searchTerm)
    }

    override fun setUpperLevel(level: Level) {
        _filter = _filter.copy(upperLevel = level)
    }

    override fun setLowerLevel(level: Level) {
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

    override fun addTrait(trait: Trait) {
        _filter = _filter.copy(traits = (_filter.traits + trait).distinct())
    }

    override fun removeTrait(trait: Trait) {
        _filter = _filter.copy(traits = _filter.traits - trait)
    }

    override fun addComplexity(complexity: Complexity) {
        _filter = _filter.copy(complexities = (_filter.complexities + complexity).distinct())
    }

    override fun removeComplexity(complexity: Complexity) {
        _filter = _filter.copy(complexities = (_filter.complexities - complexity))
    }
}