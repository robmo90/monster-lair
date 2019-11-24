package de.enduni.monsterlair.monsters.persistence.database

import de.enduni.monsterlair.monsters.datasource.MonsterDataSource
import de.enduni.monsterlair.monsters.datasource.MonsterEntityMapper
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.InternalCoroutinesApi
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.withContext

@InternalCoroutinesApi
class MonsterDatabaseInitializer(
    private val mapper: MonsterEntityMapper,
    private val dao: MonsterDao,
    private val dataSource: MonsterDataSource
) {

    suspend fun feedMonsters() = withContext(Dispatchers.IO) {
        dataSource.getMonsters()
            .collect { monsterDtos ->
                val entities = monsterDtos.map { mapper.toEntity(it) }
                dao.insertMonsters(entities)
            }
    }


}