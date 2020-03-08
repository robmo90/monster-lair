package de.enduni.monsterlair.monsters.domain

import de.enduni.monsterlair.monsters.persistence.MonsterRepository

class SaveMonsterUseCase(
    private val monsterRepository: MonsterRepository
) {

    suspend fun execute(monster: Monster) {
        return monsterRepository.saveMonster(monster)
    }

}
