package de.enduni.monsterlair.monsters.datasource

import kotlinx.coroutines.flow.Flow

interface MonsterDataSource {

    suspend fun getMonsters(): Flow<List<MonsterDto>>

}