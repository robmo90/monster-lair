package de.enduni.monsterlair.common.persistence

import androidx.room.*
import androidx.sqlite.db.SupportSQLiteQuery

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


    @Query("SELECT * FROM monsters WHERE id is :id LIMIT 1")
    suspend fun getMonster(id: Long): MonsterEntity

}