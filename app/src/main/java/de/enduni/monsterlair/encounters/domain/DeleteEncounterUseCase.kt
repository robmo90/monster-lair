package de.enduni.monsterlair.encounters.domain

import de.enduni.monsterlair.encounters.persistence.EncounterRepository

class DeleteEncounterUseCase(private val repository: EncounterRepository) {

    suspend fun execute(id: Long) {
        repository.deleteEncounter(id)
    }

}
