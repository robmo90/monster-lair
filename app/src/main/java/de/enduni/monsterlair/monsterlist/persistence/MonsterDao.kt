package de.enduni.monsterlair.monsterlist.persistence

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface MonsterDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertMonsters(monsters: List<MonsterEntity>)

    @Query("SELECT * FROM monsters ORDER BY name")
    suspend fun getAllMonsters(): List<MonsterEntity>

    @Query("SELECT * FROM monsters WHERE (name LIKE :filterString OR family LIKE :filterString) AND level BETWEEN :lowerLevel AND :higherLevel ORDER BY name")
    suspend fun getFilteredMonsters(
        filterString: String,
        lowerLevel: Int,
        higherLevel: Int
    ): List<MonsterEntity>

}