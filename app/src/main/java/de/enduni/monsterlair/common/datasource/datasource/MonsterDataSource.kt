package de.enduni.monsterlair.common.datasource.datasource

interface MonsterDataSource {

    suspend fun getMonsters(): List<MonsterDto>

    suspend fun getMonsterUpdate(version: Long): List<MonsterDto>

}