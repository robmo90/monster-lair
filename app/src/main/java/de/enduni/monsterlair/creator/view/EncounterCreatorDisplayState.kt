package de.enduni.monsterlair.creator.view

import de.enduni.monsterlair.encounters.domain.model.Encounter


data class EncounterCreatorDisplayState(
    val encounterName: String,
    val list: List<EncounterCreatorDisplayModel> = listOf()
)

sealed class EncounterCreatorAction {

    class EditEncounterClicked(val encounter: Encounter) : EncounterCreatorAction()
    class DangerLinkClicked(val url: String) : EncounterCreatorAction()
    data class CustomMonsterDelete(val id: String, val name: String) : EncounterCreatorAction()
    data class CustomMonsterEdit(val id: String) : EncounterCreatorAction()
    class DangerAdded(val name: String) : EncounterCreatorAction()
    object EncounterSaved : EncounterCreatorAction()
    object ScrollUp : EncounterCreatorAction()
    class OnGiveTreasureRecommendationClicked(val htmlTemplate: String) : EncounterCreatorAction()
    object RandomEncounterError : EncounterCreatorAction()

}