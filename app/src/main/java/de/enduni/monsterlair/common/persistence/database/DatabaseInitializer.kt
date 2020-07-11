package de.enduni.monsterlair.common.persistence.database

import de.enduni.monsterlair.common.datasource.datasource.MonsterDataSource
import de.enduni.monsterlair.common.datasource.datasource.MonsterDto
import de.enduni.monsterlair.common.datasource.hazard.HazardDataSource
import de.enduni.monsterlair.common.datasource.hazard.HazardDto
import de.enduni.monsterlair.common.persistence.HazardDao
import de.enduni.monsterlair.common.persistence.MonsterDao
import de.enduni.monsterlair.monsters.persistence.MonsterEntityMapper
import de.enduni.monsterlair.update.UpdateManager
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class DatabaseInitializer(
    private val monsterEntityMapper: MonsterEntityMapper,
    private val monsterDao: MonsterDao,
    private val monsterDataSource: MonsterDataSource,
    private val hazardDataSource: HazardDataSource,
    private val hazardEntityMapper: HazardEntityMapper,
    private val hazardDao: HazardDao,
    private val updateManager: UpdateManager
) {

    suspend fun feedMonsters() = withContext(Dispatchers.IO) {
        val savedVersion = updateManager.savedVersion
        if (savedVersion < 11) {
            monsterDataSource.getMonsters().insertMonsters()
            hazardDataSource.getHazards().saveHazards()
        }
    }

    private suspend fun List<MonsterDto>.insertMonsters() {
        this.map { monsterEntityMapper.toEntity(it) }
            .chunked(20)
            .forEach { monsterDao.insertMonsters(it) }
    }

    private suspend fun List<HazardDto>.saveHazards() {
        this.map { hazardEntityMapper.toEntity(it) }
            .chunked(20)
            .forEach { hazardDao.insertHazards(it) }
    }
}