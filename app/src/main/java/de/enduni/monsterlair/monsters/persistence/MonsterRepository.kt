package de.enduni.monsterlair.monsters.persistence

import androidx.sqlite.db.SimpleSQLiteQuery
import de.enduni.monsterlair.common.datasource.monsters.MonsterDto
import de.enduni.monsterlair.common.domain.MonsterType
import de.enduni.monsterlair.common.persistence.MonsterDao
import de.enduni.monsterlair.common.persistence.MonsterEntity
import de.enduni.monsterlair.monsters.domain.Monster
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import timber.log.Timber


class MonsterRepository(
    private val monsterDao: MonsterDao,
    private val monsterEntityMapper: MonsterEntityMapper
) {

    suspend fun getMonster(id: String) =
        monsterDao.getMonster(id).let { monsterEntityMapper.toModel(it) }

    suspend fun getMonsters(
        filter: String?,
        lowerLevel: Int,
        higherLevel: Int,
        sortBy: String,
        monsterTypes: List<MonsterType>
    ): List<Monster> {
        val query = buildQuery(filter, monsterTypes, lowerLevel, higherLevel, sortBy)
        return monsterDao.getFilteredMonsters(SimpleSQLiteQuery(query)).map { it.monster }
            .toDomain()
    }

    suspend fun saveMonster(monster: Monster) {
        val entity = monsterEntityMapper.toEntity(monster)
        monsterDao.insertMonster(entity)
    }

    suspend fun saveMonster(monster: MonsterDto) {

        val entity = monsterEntityMapper.toEntity(monster)
        monsterDao.insertMonster(entity)
    }

    suspend fun deleteMonster(id: String) {
        monsterDao.deleteMonster(id)
    }

    fun getMonsterFlow(
        filter: String?,
        lowerLevel: Int,
        higherLevel: Int,
        sortBy: String,
        monsterTypes: List<MonsterType>
    ): Flow<List<Monster>> {
        val query = buildQuery(filter, monsterTypes, lowerLevel, higherLevel, sortBy)
        return monsterDao.getFilteredMonsterFlow(SimpleSQLiteQuery(query))
            .map { it.toDomain() }
    }

    private fun buildQuery(
        filter: String?,
        monsterTypes: List<MonsterType>,
        lowerLevel: Int,
        higherLevel: Int,
        sortBy: String
    ): String {
        val filterString = if (filter.isNullOrEmpty()) "\"%\"" else "\"%${filter}%\""
        val typeFilterString = if (monsterTypes.isEmpty()) {
            ""
        } else {
            "AND TYPE IN ${monsterTypes.joinToString(
                prefix = "(\"",
                postfix = "\")",
                separator = "\", \""
            )}"
        }
        val query = "SELECT * FROM monsters WHERE " +
                "(name LIKE $filterString OR family LIKE $filterString) " +
                "AND level BETWEEN $lowerLevel AND $higherLevel " +
                typeFilterString +
                "ORDER BY $sortBy ASC"
        Timber.v("Using $query")
        return query
    }


    private fun List<MonsterEntity>.toDomain(): List<Monster> {
        return this.map { monsterEntityMapper.toModel(it) }
    }

}