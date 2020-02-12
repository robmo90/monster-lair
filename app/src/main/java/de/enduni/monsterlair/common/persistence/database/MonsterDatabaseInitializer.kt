package de.enduni.monsterlair.common.persistence.database

import de.enduni.monsterlair.common.datasource.datasource.MonsterDataSource
import de.enduni.monsterlair.common.datasource.datasource.MonsterDto
import de.enduni.monsterlair.common.datasource.hazard.HazardDataSource
import de.enduni.monsterlair.common.persistence.HazardDao
import de.enduni.monsterlair.common.persistence.MonsterDao
import de.enduni.monsterlair.common.persistence.MonsterEntity
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
        if (allMonsters.getLastId() <= 487L) {
            Timber.d("Insert monster update")
            monsterDataSource.getMonsterUpdate(1L)
                .insertMonsters()
        }
    }

    private suspend fun List<MonsterDto>.insertMonsters() {
        this.map { monsterEntityMapper.toEntity(it) }
            .chunked(50)
            .forEach { monsterDao.insertMonsters(it) }
    }


    private fun List<MonsterEntity>.getLastId(): Long {
        return this.maxBy { it.id }?.id ?: 0
    }

}