package de.enduni.monsterlair.monsters.view

data class MonsterOverviewViewState(
    val monsters: List<MonsterListDisplayModel> = listOf(),
    val filter: MonsterFilter? = null
)

data class MonsterFilter(
    val string: String? = null,
    val lowerLevel: Int = DEFAULT_LEVEL_LOWER,
    val upperLevel: Int = DEFAULT_LEVEL_UPPER,
    val sortBy: SortBy = SortBy.NAME
) {

    companion object {

        const val DEFAULT_LEVEL_LOWER = -1
        const val DEFAULT_LEVEL_UPPER = 25

    }
}

enum class SortBy(val value: String) {
    NAME("name"),
    LEVEL("level"),
    TYPE("type")
}

