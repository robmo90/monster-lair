package de.enduni.monsterlair.common.datasource.datasource

interface MonsterDataSource {

    suspend fun getMonsters(): List<MonsterDto>

}