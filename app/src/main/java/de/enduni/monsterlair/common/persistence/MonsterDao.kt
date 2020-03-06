package de.enduni.monsterlair.common.persistence

import androidx.room.*
import androidx.sqlite.db.SupportSQLiteQuery
import kotlinx.coroutines.flow.Flow

@Dao
interface MonsterDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertMonsters(monsters: List<MonsterEntity>)

    @Query("SELECT * FROM monsters ORDER BY name")
    suspend fun getAllMonsters(): List<MonsterEntity>


    @RawQuery
    suspend fun getFilteredMonsters(
        sqLiteQuery: SupportSQLiteQuery
    ): List<MonsterEntity>

    @RawQuery(observedEntities = [MonsterEntity::class])
    fun getFilteredMonsterFlow(
        sqLiteQuery: SupportSQLiteQuery
    ): Flow<List<MonsterEntity>>

    @Query("SELECT * FROM monsters WHERE id is :id LIMIT 1")
    suspend fun getMonster(id: Long): MonsterEntity

}