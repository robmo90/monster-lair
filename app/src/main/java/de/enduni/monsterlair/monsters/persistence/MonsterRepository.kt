package de.enduni.monsterlair.monsters.persistence

import androidx.sqlite.db.SimpleSQLiteQuery
import de.enduni.monsterlair.common.domain.MonsterType
import de.enduni.monsterlair.common.persistence.MonsterDao
import de.enduni.monsterlair.common.persistence.MonsterEntity
import de.enduni.monsterlair.monsters.domain.Monster
import timber.log.Timber


class MonsterRepository(
    private val monsterDao: MonsterDao,
    private val monsterEntityMapper: MonsterEntityMapper
) {

    suspend fun getMonster(id: Long) =
        monsterDao.getMonster(id).let { monsterEntityMapper.toModel(it) }

    suspend fun getMonsters(
        filter: String?,
        lowerLevel: Int,
        higherLevel: Int,
        sortBy: String,
        monsterTypes: List<MonsterType>
    ): List<Monster> {
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
        return monsterDao.getFilteredMonsters(SimpleSQLiteQuery(query)).toDomain()
    }


    private fun List<MonsterEntity>.toDomain(): List<Monster> {
        return this.map { monsterEntityMapper.toModel(it) }
    }

}