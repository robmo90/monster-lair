package de.enduni.monsterlair.common.persistence

import androidx.room.*
import androidx.sqlite.db.SupportSQLiteQuery

@Dao
interface HazardDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertHazards(hazards: List<HazardEntity>)

    @Query("SELECT * FROM hazards ORDER BY name")
    suspend fun getAllHazards(): List<HazardEntity>

    @RawQuery
    suspend fun getFilteredHazards(
        sqLiteQuery: SupportSQLiteQuery
    ): List<HazardEntity>

    @Query("SELECT * FROM hazards WHERE id is :id LIMIT 1")
    suspend fun getHazard(id: Long): HazardEntity

}