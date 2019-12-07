package de.enduni.monsterlair.hazards.view

import de.enduni.monsterlair.common.domain.Complexity

data class HazardDisplayModel(
    val id: Long,
    val name: String,
    val url: String,
    val level: Int,
    val complexity: Complexity,
    val source: String
)

sealed class HazardOverviewAction {

    class HazardSelected(val url: String) : HazardOverviewAction()

}