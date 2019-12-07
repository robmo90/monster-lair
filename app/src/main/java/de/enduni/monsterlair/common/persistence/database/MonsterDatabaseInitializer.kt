package de.enduni.monsterlair.common.persistence.database

import de.enduni.monsterlair.common.datasource.datasource.MonsterDataSource
import de.enduni.monsterlair.common.datasource.hazard.HazardDataSource
import de.enduni.monsterlair.common.persistence.HazardDao
import de.enduni.monsterlair.common.persistence.MonsterDao
import de.enduni.monsterlair.monsters.persistence.MonsterEntityMapper
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import timber.log.Timber

class MonsterDatabaseInitializer(
    private val monsterEntityMapper: MonsterEntityMapper,
    private val monsterDao: MonsterDao,
    private val monsterDataSource: MonsterDataSource,
    private val hazardDataSource: HazardDataSource,
    private val hazardEntityMapper: HazardEntityMapper,
    private val hazardDao: HazardDao
) {

    suspend fun feedMonsters() = withContext(Dispatchers.IO) {
        if (monsterDao.getAllMonsters().isEmpty()) {
            Timber.d("Feeding monsters")
            val monsters = monsterDataSource.getMonsters().map { monsterEntityMapper.toEntity(it) }
            monsterDao.insertMonsters(monsters)
        }
        if (hazardDao.getAllHazards().isEmpty()) {
            Timber.d("Setting up traps")
            val hazards = hazardDataSource.getHazards().map { hazardEntityMapper.toEntity(it) }
            hazardDao.insertHazards(hazards)
        }
    }


}