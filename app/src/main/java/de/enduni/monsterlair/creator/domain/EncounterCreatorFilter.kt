package de.enduni.monsterlair.creator.domain

import de.enduni.monsterlair.common.domain.*
import de.enduni.monsterlair.creator.view.DangerType
import de.enduni.monsterlair.monsters.domain.MonsterFilter

data class EncounterCreatorFilter(
    val searchTerm: String = "",
    val lowerLevel: Level = MonsterFilter.DEFAULT_LEVEL_LOWER,
    val upperLevel: Level = MonsterFilter.DEFAULT_LEVEL_UPPER,
    val types: List<MonsterType> = emptyList(),
    val complexities: List<Complexity> = emptyList(),
    val dangerTypes: List<DangerType> = emptyList(),
    val alignments: List<Alignment> = emptyList(),
    val rarities: List<Rarity> = emptyList(),
    val sizes: List<Size> = emptyList(),
    val traits: List<Trait> = emptyList(),
    val sortBy: SortBy = SortBy.NAME,
    val withinBudget: Boolean = true,
    var refresh: Int = 0
)