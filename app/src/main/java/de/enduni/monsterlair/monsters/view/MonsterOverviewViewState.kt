package de.enduni.monsterlair.monsters.view

import de.enduni.monsterlair.monsters.domain.MonsterFilter

data class MonsterOverviewViewState(
    val monsters: List<MonsterListDisplayModel> = listOf(),
    val filter: MonsterFilter? = null
)