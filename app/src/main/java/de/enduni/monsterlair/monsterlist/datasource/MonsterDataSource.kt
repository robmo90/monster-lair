package de.enduni.monsterlair.monsterlist.datasource

import kotlinx.coroutines.flow.Flow

interface MonsterDataSource {

    suspend fun getMonsters(): Flow<List<MonsterDto>>

}