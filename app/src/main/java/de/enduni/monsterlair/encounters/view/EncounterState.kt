package de.enduni.monsterlair.encounters.view

import de.enduni.monsterlair.encounters.domain.model.EncounterDifficulty

data class EncounterState(
    val encounters: List<EncounterDisplayModel>? = null
)

sealed class EncounterAction {

    data class EncounterSelectedAction(
        val encounterName: String,
        val numberOfPlayers: Int,
        val encounterLevel: Int,
        val encounterId: Long,
        val useProficiencyWithoutLevel: Boolean,
        val notes: String,
        val difficulty: EncounterDifficulty
    ) : EncounterAction()

    data class EncounterDetailsOpenedAction(val encounterName: String, val id: Long) :
        EncounterAction()

    data class ExportEncounterToPdfAction(
        val encounterName: String,
        val template: String
    ) : EncounterAction()

}
