package de.enduni.monsterlair.monsters.domain

import de.enduni.monsterlair.encounters.persistence.EncounterRepository
import de.enduni.monsterlair.monsters.persistence.MonsterRepository

class DeleteMonsterUseCase(
    private val monsterRepository: MonsterRepository,
    private val encounterRepository: EncounterRepository
) {

    suspend fun execute(id: String) {
        encounterRepository.getAllEncounters()
            .filter { encounter -> encounter.monsters.any { it.monster.id == id } }
            .forEach { encounterRepository.deleteEncounter(it.id!!) }
        monsterRepository.deleteMonster(id)
    }

}
