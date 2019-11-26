package de.enduni.monsterlair.encounters.domain

import de.enduni.monsterlair.encounters.persistence.EncounterRepository

class RetrieveEncountersUseCase(
    private val encounterRepository: EncounterRepository
) {

    suspend fun execute() = encounterRepository.getEncounters()

}