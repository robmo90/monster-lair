package de.enduni.monsterlair.common.persistence.database

import de.enduni.monsterlair.common.persistence.HazardDao
import de.enduni.monsterlair.common.persistence.MonsterDao
import de.enduni.monsterlair.hazards.datasource.HazardDataSource
import de.enduni.monsterlair.monsters.datasource.MonsterDataSource
import de.enduni.monsterlair.monsters.persistence.MonsterEntityMapper
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class MonsterDatabaseInitializer(
    private val monsterEntityMapper: MonsterEntityMapper,
    private val monsterDao: MonsterDao,
    private val monsterDataSource: MonsterDataSource,
    private val hazardDataSource: HazardDataSource,
    private val hazardEntityMapper: HazardEntityMapper,
    private val hazardDao: HazardDao
) {

    suspend fun feedMonsters() = withContext(Dispatchers.IO) {
        val monsters = monsterDataSource.getMonsters().map { monsterEntityMapper.toEntity(it) }
        monsterDao.insertMonsters(monsters)
        val hazards = hazardDataSource.getHazards().map { hazardEntityMapper.toEntity(it) }
        hazardDao.insertHazards(hazards)
    }


}