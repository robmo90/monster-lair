package de.enduni.monsterlair.overview

import de.enduni.monsterlair.common.Monster

data class MonsterOverviewViewState(
    val monsters: List<Monster> = listOf(),
    val filter: MonsterFilter? = null
)