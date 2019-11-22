package de.enduni.monsterlair.overview

class RetrieveMonstersUseCase(private val monsterRepository: MonsterRepository) {

    suspend fun execute() = monsterRepository.getMonsters()

}