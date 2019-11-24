package de.enduni.monsterlair.monsters.persistence.database

import androidx.room.*
import androidx.sqlite.db.SupportSQLiteQuery
import de.enduni.monsterlair.monsters.persistence.MonsterEntity

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

}