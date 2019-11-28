package de.enduni.monsterlair.encounters.creator.domain

import de.enduni.monsterlair.encounters.domain.model.Encounter
import de.enduni.monsterlair.encounters.persistence.EncounterRepository

class StoreEncounterUseCase(private val encounterRepository: EncounterRepository) {

    suspend fun store(encounter: Encounter) {
        encounterRepository.saveEncounter(encounter)
    }

}