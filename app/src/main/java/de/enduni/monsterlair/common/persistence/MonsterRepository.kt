package de.enduni.monsterlair.common.persistence

import androidx.sqlite.db.SimpleSQLiteQuery
import de.enduni.monsterlair.monsters.datasource.MonsterEntityMapper
import de.enduni.monsterlair.monsters.domain.Monster
import timber.log.Timber


class MonsterRepository(
    private val monsterDao: MonsterDao,
    private val monsterEntityMapper: MonsterEntityMapper
) {

    suspend fun getMonster(id: Long) =
        monsterDao.getMonster(id).let { monsterEntityMapper.toModel(it) }

    suspend fun getMonsters(
        filterString: String,
        lowerLevel: Int,
        higherLevel: Int,
        sortBy: String
    ): List<Monster> {
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