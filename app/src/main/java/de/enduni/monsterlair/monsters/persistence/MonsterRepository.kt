package de.enduni.monsterlair.monsters.persistence

import androidx.sqlite.db.SimpleSQLiteQuery
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
        sortBy: String
    ): List<Monster> {
        val filterString = if (filter.isNullOrEmpty()) "\"%\"" else "\"%${filter}%\""
        val query =
            "SELECT * FROM monsters WHERE (name LIKE $filterString OR family LIKE $filterString) AND level BETWEEN $lowerLevel AND $higherLevel ORDER BY $sortBy ASC"
        Timber.v("Using $query")
        return monsterDao.getFilteredMonsters(SimpleSQLiteQuery(query)).toDomain()
    }


    private fun List<MonsterEntity>.toDomain(): List<Monster> {
        Timber.v("These are my entities: $this")
        return this.map { monsterEntityMapper.toModel(it) }
    }

}