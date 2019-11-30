package de.enduni.monsterlair.encounters.view

import de.enduni.monsterlair.encounters.domain.model.EncounterDifficulty

data class EncounterState(
    val numberOfPlayers: Int? = null,
    val levelOfPlayers: Int? = null,
    val levelValid: Boolean = true,
    val numberValid: Boolean = true,
    val difficulty: EncounterDifficulty = EncounterDifficulty.MODERATE,
    val encounters: List<EncounterDisplayModel> = listOf()
) {

    fun isStartAllowed() =
        numberOfPlayers != null && levelOfPlayers != null && levelValid && numberValid

}

sealed class EncounterAction {

    data class EncounterSelectedAction(
        val numberOfPlayers: Int,
        val encounterLevel: Int,
        val encounterId: Long,
        val difficulty: EncounterDifficulty
    ) : EncounterAction()

    data class EncounterDetailsOpenedAction(val encounterName: String, val id: Long) :
        EncounterAction()

    data class ExportEncounterToPdfAction(
        val encounterName: String,
        val template: String
    ) : EncounterAction()

}
