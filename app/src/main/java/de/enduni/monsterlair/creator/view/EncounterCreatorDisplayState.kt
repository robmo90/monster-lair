package de.enduni.monsterlair.creator.view

import de.enduni.monsterlair.encounters.domain.model.Encounter
import de.enduni.monsterlair.monsters.domain.Monster


data class EncounterCreatorDisplayState(
    val encounterName: String,
    val list: List<EncounterCreatorDisplayModel> = listOf()
)

sealed class EncounterCreatorAction {

    class EditEncounterClicked(val encounter: Encounter) : EncounterCreatorAction()
    class DangerLinkClicked(val url: String) : EncounterCreatorAction()
    object CustomMonsterClicked : EncounterCreatorAction()
    class DangerAdded(val name: String) : EncounterCreatorAction()
    object EncounterSaved : EncounterCreatorAction()
    object ScrollUp : EncounterCreatorAction()
    object ShowCreatorHint : EncounterCreatorAction()
    class OnCustomMonsterPressed(val id: String, val monsterName: String) : EncounterCreatorAction()
    class OnEditCustomMonsterClicked(val monster: Monster) : EncounterCreatorAction()
    class OnGiveTreasureRecommendationClicked(val htmlTemplate: String) : EncounterCreatorAction()
    object RandomEncounterError : EncounterCreatorAction()

}