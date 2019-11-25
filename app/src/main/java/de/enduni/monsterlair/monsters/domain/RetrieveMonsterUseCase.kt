package de.enduni.monsterlair.monsters.domain

import de.enduni.monsterlair.common.persistence.MonsterRepository

class RetrieveMonsterUseCase(private val monsterRepository: MonsterRepository) {

    suspend fun execute(name: String): Monster {
        return monsterRepository.getMonster(name)
    }

}

