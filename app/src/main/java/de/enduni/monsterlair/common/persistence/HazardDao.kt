package de.enduni.monsterlair.common.persistence

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface HazardDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertHazards(hazards: List<HazardEntity>)

    @Query("SELECT * FROM hazards ORDER BY name")
    suspend fun getAllHazards(): List<HazardEntity>

    @Query("SELECT * FROM hazards WHERE id is :id LIMIT 1")
    suspend fun getHazard(id: Long): HazardEntity

}