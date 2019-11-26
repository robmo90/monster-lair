package de.enduni.monsterlair.common.persistence.database

import de.enduni.monsterlair.common.persistence.MonsterDao
import de.enduni.monsterlair.monsters.datasource.MonsterDataSource
import de.enduni.monsterlair.monsters.persistence.MonsterEntityMapper
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class MonsterDatabaseInitializer(
    private val mapper: MonsterEntityMapper,
    private val dao: MonsterDao,
    private val dataSource: MonsterDataSource
) {

    suspend fun feedMonsters() = withContext(Dispatchers.IO) {
        val entities = dataSource.getMonsters().map { mapper.toEntity(it) }
        dao.insertMonsters(entities)
    }


}