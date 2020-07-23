package de.enduni.monsterlair.creator.view

import de.enduni.monsterlair.common.domain.*
import de.enduni.monsterlair.common.filter.*
import de.enduni.monsterlair.creator.domain.EncounterCreatorFilter
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow

class EncounterCreatorFilterStore :
    SearchFilterStore,
    LevelFilterStore,
    SortByFilterStore,
    RarityFilterStore,
    TraitFilterStore,
    MonsterTypeFilterStore,
    AlignmentFilterStore,
    SizeFilterStore,
    ComplexityFilterStore,
    DangerTypeFilterStore,
    BudgetFilterStore,
    RefreshFilterStore {

    private val _filterFlow = MutableStateFlow(EncounterCreatorFilter())
    val filter: Flow<EncounterCreatorFilter> get() = _filterFlow

    private var _filter: EncounterCreatorFilter
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

    override fun addType(type: MonsterType) {
        _filter = _filter.copy(types = (_filter.types + type).distinct())
    }

    override fun removeType(type: MonsterType) {
        _filter = _filter.copy(types = (_filter.types - type))
    }

    override fun addAlignment(alignment: Alignment) {
        _filter = _filter.copy(alignments = (_filter.alignments + alignment).distinct())
    }

    override fun removeAlignment(alignment: Alignment) {
        _filter = _filter.copy(alignments = (_filter.alignments - alignment))
    }

    override fun addSize(size: Size) {
        _filter = _filter.copy(sizes = (_filter.sizes + size).distinct())
    }

    override fun removeSize(size: Size) {
        _filter = _filter.copy(sizes = (_filter.sizes - size))
    }

    override fun refresh() {
        _filter = _filter.copy(refresh = (_filter.refresh++))
    }

    override fun addComplexity(complexity: Complexity) {
        _filter = _filter.copy(complexities = (_filter.complexities + complexity).distinct())
    }

    override fun removeComplexity(complexity: Complexity) {
        _filter = _filter.copy(complexities = (_filter.complexities - complexity))
    }

    override fun addDangerType(dangerType: DangerType) {
        _filter = _filter.copy(dangerTypes = (_filter.dangerTypes + dangerType).distinct())
    }

    override fun removeDangerType(dangerType: DangerType) {
        _filter = _filter.copy(dangerTypes = (_filter.dangerTypes - dangerType))
    }

    override fun setFilterWithinBudget(withinBudget: Boolean) {
        _filter = _filter.copy(withinBudget = withinBudget)
    }
}