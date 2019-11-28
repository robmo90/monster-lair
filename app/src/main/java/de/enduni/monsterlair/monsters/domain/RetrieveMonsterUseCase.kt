package de.enduni.monsterlair.monsters.domain

import de.enduni.monsterlair.monsters.persistence.MonsterRepository

class RetrieveMonsterUseCase(
    private val monsterRepository: MonsterRepository
) {

    suspend fun execute(id: Long): Monster {
        return monsterRepository.getMonster(id)
    }

}
