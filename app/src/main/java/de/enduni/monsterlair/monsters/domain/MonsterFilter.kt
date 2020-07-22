package de.enduni.monsterlair.monsters.domain

import de.enduni.monsterlair.common.domain.*

data class MonsterFilter(
    val searchTerm: String = "",
    val lowerLevel: Level = DEFAULT_LEVEL_LOWER,
    val upperLevel: Level = DEFAULT_LEVEL_UPPER,
    val types: List<MonsterType> = emptyList(),
    val alignments: List<Alignment> = emptyList(),
    val rarities: List<Rarity> = emptyList(),
    val sizes: List<Size> = emptyList(),
    val traits: List<Trait> = emptyList(),
    val sortBy: SortBy = SortBy.NAME,
    var refresh: Int = 0
) {

    companion object {

        const val DEFAULT_LEVEL_LOWER = -1
        const val DEFAULT_LEVEL_UPPER = 25

    }
}