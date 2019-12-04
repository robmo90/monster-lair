package de.enduni.monsterlair.encounters.view

import android.content.Context
import de.enduni.monsterlair.R
import de.enduni.monsterlair.encounters.domain.model.Encounter

class EncounterDisplayModelMapper(
    private val context: Context
) {


    fun mapToDisplayModel(encounter: Encounter): EncounterDisplayModel {
        val monsterString = encounter.monsters.joinToString { encounterMonster ->
            "${encounterMonster.count} ${encounterMonster.monster.name}"
        }
        val hazardString = encounter.hazards.joinToString { encounterHazard ->
            "${encounterHazard.count} ${encounterHazard.hazard.name}"
        }
        val dangerString = when {
            monsterString.isNotBlank() && hazardString.isNotBlank() -> "$monsterString, $hazardString"
            monsterString.isNotBlank() && hazardString.isBlank() -> monsterString
            monsterString.isBlank() && hazardString.isNotBlank() -> hazardString
            else -> context.getString(R.string.no_dangers_encounter)
        }
        return EncounterDisplayModel(
            id = encounter.id!!,
            name = encounter.name,
            level = encounter.level,
            numberOfPlayers = encounter.numberOfPlayers,
            difficulty = encounter.currentDifficulty,
            xp = encounter.currentBudget,
            dangers = dangerString
        )
    }


}