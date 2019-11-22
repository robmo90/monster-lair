package de.enduni.monsterlair.monsterlist.view

data class MonsterOverviewViewState(
    val monsters: List<MonsterListDisplayModel> = listOf(),
    val filter: MonsterFilter? = null
)

data class MonsterFilter(
    val string: String? = null,
    val lowerLevel: Int = DEFAULT_LEVEL_LOWER,
    val upperLevel: Int = DEFAULT_LEVEL_UPPER
) {

    companion object {

        const val DEFAULT_LEVEL_LOWER = -1
        const val DEFAULT_LEVEL_UPPER = 25

    }
}