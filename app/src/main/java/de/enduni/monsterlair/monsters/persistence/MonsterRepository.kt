package de.enduni.monsterlair.monsters.persistence

import androidx.sqlite.db.SimpleSQLiteQuery
import de.enduni.monsterlair.monsters.datasource.MonsterEntityMapper
import de.enduni.monsterlair.monsters.domain.Monster
import de.enduni.monsterlair.monsters.persistence.database.MonsterDao
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf
import timber.log.Timber


class MonsterRepository(
    private val monsterDao: MonsterDao,
    private val monsterEntityMapper: MonsterEntityMapper
) {

    suspend fun getAllMonsters(): Flow<List<Monster>> =
        flowOf(monsterDao.getAllMonsters().toDomain())

    suspend fun getMonsters(
        filterString: String,
        lowerLevel: Int,
        higherLevel: Int,
        sortBy: String
    ): Flow<List<Monster>> {
        val query =
            "SELECT * FROM monsters WHERE (name LIKE $filterString OR family LIKE $filterString) AND level BETWEEN $lowerLevel AND $higherLevel ORDER BY $sortBy ASC"
        Timber.d("Using $query")
        val value = monsterDao.getFilteredMonsters(SimpleSQLiteQuery(query)).toDomain()
        return flowOf(value)
    }


    private fun List<MonsterEntity>.toDomain(): List<Monster> {
        return this.map { monsterEntityMapper.toModel(it) }
    }

}