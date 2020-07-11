package de.enduni.monsterlair.monsters.domain

import de.enduni.monsterlair.monsters.persistence.MonsterRepository

class DeleteMonsterUseCase(
    private val monsterRepository: MonsterRepository
) {

    suspend fun execute(id: String) {
        return monsterRepository.deleteMonster(id)
    }

}
