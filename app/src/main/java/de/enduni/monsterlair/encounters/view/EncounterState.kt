package de.enduni.monsterlair.encounters.view

import de.enduni.monsterlair.common.EncounterDifficulty

data class EncounterState(
    val numberOfPlayers: Int? = null,
    val levelOfPlayers: Int? = null,
    val levelValid: Boolean = true,
    val numberValid: Boolean = true,
    val difficulty: EncounterDifficulty = EncounterDifficulty.TRIVIAL
) {

    fun isStartAllowed() =
        numberOfPlayers != null && levelOfPlayers != null && levelValid && numberValid

}