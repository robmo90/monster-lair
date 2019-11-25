package de.enduni.monsterlair.monsters.datasource

interface MonsterDataSource {

    suspend fun getMonsters(): List<MonsterDto>

}