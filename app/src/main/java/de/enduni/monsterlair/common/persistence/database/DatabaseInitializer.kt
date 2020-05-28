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
import timber.log.Timber

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
        val allMonsters = monsterDao.getAllMonsters()
        if (allMonsters.isEmpty()) {
            Timber.d("Feeding monsters")
            monsterDataSource.getMonsters()
                .insertMonsters()
        }
        if (hazardDao.getAllHazards().isEmpty()) {
            Timber.d("Setting up traps")
            hazardDataSource.getHazards()
                .map { hazardEntityMapper.toEntity(it) }
                .chunked(50)
                .forEach { hazardDao.insertHazards(it) }
        }
        if (monsterDao.getHighestId() <= 487L) {
            Timber.d("Insert monster update")
            monsterDataSource.getMonsterUpdate(1L)
                .insertMonsters()
        }
        if (updateManager.savedVersion < 10) {
            Timber.d("Insert monster update 2 + 3")
            monsterDataSource.getMonsterUpdate(2L)
                .saveMonsters()
            monsterDataSource.getMonsterUpdate(3L)
                .saveMonsters()
            hazardDataSource.getHazardUpdate(1L)
                .saveHazards()
        }
        Timber.d("Highest ID is: ${hazardDao.getHighestId()} ")
    }

    private suspend fun List<MonsterDto>.insertMonsters() {
        this.map { monsterEntityMapper.toEntity(it) }
            .chunked(50)
            .forEach { monsterDao.insertMonsters(it) }
    }

    private suspend fun List<MonsterDto>.saveMonsters() {
        this.forEach {
            val entity = monsterEntityMapper.toEntity(it, monsterDao.getHighestId() + 1)
            Timber.d("This is my Monster: $entity")
            monsterDao.insertMonster(entity)
        }
    }

    private suspend fun List<HazardDto>.saveHazards() {
        this.forEach {
            val entity = hazardEntityMapper.toEntity(it, hazardDao.getHighestId() + 1)
            Timber.d("This is my Monster: $entity")
            hazardDao.insertHazard(entity)
        }
    }

}