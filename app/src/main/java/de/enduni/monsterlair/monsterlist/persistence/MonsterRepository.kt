package de.enduni.monsterlair.monsterlist.persistence

import de.enduni.monsterlair.monsterlist.datasource.MonsterEntityMapper
import de.enduni.monsterlair.monsterlist.domain.Monster
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf


class MonsterRepository(
    private val monsterDao: MonsterDao,
    private val monsterEntityMapper: MonsterEntityMapper
) {

    suspend fun getAllMonsters(): Flow<List<Monster>> =
        flowOf(monsterDao.getAllMonsters().toDomain())

    suspend fun getMonsters(
        filterString: String,
        lowerLevel: Int,
        higherLevel: Int
    ): Flow<List<Monster>> =
        flowOf(monsterDao.getFilteredMonsters(filterString, lowerLevel, higherLevel).toDomain())


    private fun List<MonsterEntity>.toDomain(): List<Monster> {
        return this.map { monsterEntityMapper.toModel(it) }
    }

}