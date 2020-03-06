package de.enduni.monsterlair.creator.domain

import de.enduni.monsterlair.encounters.persistence.EncounterRepository

class RetrieveEncounterUseCase(
    private val encounterRepository: EncounterRepository
) {


    suspend fun execute(id: Long) = encounterRepository.getEncounter(id)

}