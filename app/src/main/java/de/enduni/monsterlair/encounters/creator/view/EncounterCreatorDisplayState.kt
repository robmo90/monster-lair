package de.enduni.monsterlair.encounters.creator.view

import de.enduni.monsterlair.monsters.view.MonsterFilter
import de.enduni.monsterlair.monsters.view.SortBy


data class EncounterCreatorDisplayState(
    val list: List<EncounterCreatorDisplayModel> = listOf(),
    val filter: EncounterCreatorFilter? = null
)

data class EncounterCreatorFilter(
    val string: String? = null,
    val lowerLevel: Int = MonsterFilter.DEFAULT_LEVEL_LOWER,
    val upperLevel: Int = MonsterFilter.DEFAULT_LEVEL_UPPER,
    val sortBy: SortBy = SortBy.NAME
)

sealed class EncounterCreatorAction {

    class DangerLinkClicked(val url: String) : EncounterCreatorAction()
    class DangerAdded(val name: String) : EncounterCreatorAction()
    class SaveClicked(val name: String) : EncounterCreatorAction()
    object EncounterSaved : EncounterCreatorAction()

}