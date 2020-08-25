package de.enduni.monsterlair.common.persistence.database

import de.enduni.monsterlair.common.datasource.hazard.HazardDataSource
import de.enduni.monsterlair.common.datasource.hazard.HazardDto
import de.enduni.monsterlair.common.datasource.monsters.MonsterDataSource
import de.enduni.monsterlair.common.datasource.monsters.MonsterDto
import de.enduni.monsterlair.common.datasource.treasure.TreasureDataSource
import de.enduni.monsterlair.common.datasource.treasure.TreasureDto
import de.enduni.monsterlair.common.persistence.*
import de.enduni.monsterlair.monsters.persistence.MonsterEntityMapper
import de.enduni.monsterlair.treasure.repository.TreasureEntityMapper
import de.enduni.monsterlair.update.UpdateManager
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.withContext

class DatabaseInitializer(
    private val monsterEntityMapper: MonsterEntityMapper,
    private val monsterDao: MonsterDao,
    private val monsterDataSource: MonsterDataSource,
    private val hazardDataSource: HazardDataSource,
    private val hazardEntityMapper: HazardEntityMapper,
    private val hazardDao: HazardDao,
    private val treasureDataSource: TreasureDataSource,
    private val treasureEntityMapper: TreasureEntityMapper,
    private val treasureDao: TreasureDao,
    private val updateManager: UpdateManager
) {

    private val _migrationRunning = MutableStateFlow(true)
    val migrationRunning: Flow<Boolean> get() = _migrationRunning

    suspend fun initialize() = withContext(Dispatchers.IO) {
        val savedVersion = updateManager.savedVersion
        if (savedVersion < 11) {
            monsterDataSource.getMonsters().insertMonsters()
            hazardDataSource.getHazards().saveHazards()
            treasureDataSource.getTreasures().saveTreasures()
        }
        if (savedVersion in 11..12) {
            treasureDataSource.getTreasures().saveTreasures()
        }

        _migrationRunning.value = false
    }

    private suspend fun List<MonsterDto>.insertMonsters() {
        this.map { monsterEntityMapper.toEntity(it) }
            .forEach { (entity, traits) ->
                monsterDao.insertMonster(entity)
                monsterDao.insertTraits(traits)
                monsterDao.insertCrossRef(traits.map {
                    MonsterAndTraitsCrossRef(
                        entity.id,
                        it.name
                    )
                })
            }
    }

    private suspend fun List<HazardDto>.saveHazards() {
        this.map { hazardEntityMapper.fromDtoToEntity(it) }
            .forEach { (entity, traits) ->
                hazardDao.insertHazard(entity)
                hazardDao.insertTraits(traits)
                hazardDao.insertCrossRef(traits.map {
                    HazardsAndTraitsCrossRef(
                        entity.id,
                        it.name
                    )
                })
            }
    }

    private suspend fun List<TreasureDto>.saveTreasures() {
        this.map { treasureEntityMapper.fromDtoToEntity(it) }
            .forEach { (entity, traits) ->
                treasureDao.insertTreasure(entity)
                treasureDao.insertTraits(traits)
                treasureDao.insertCrossRef(traits.map {
                    TreasureAndTraitsCrossRef(
                        entity.id,
                        it.name
                    )
                })
            }
    }
}