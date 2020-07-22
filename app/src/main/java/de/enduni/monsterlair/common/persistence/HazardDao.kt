package de.enduni.monsterlair.common.persistence

import androidx.room.*
import androidx.sqlite.db.SupportSQLiteQuery

@Dao
interface HazardDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertHazards(hazards: List<HazardEntity>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertHazard(hazard: HazardEntity)

    @Transaction
    @Query("SELECT * FROM hazards ORDER BY name")
    suspend fun getAllHazards(): List<HazardWithTraits>

    @Query("SELECT MAX(id) FROM hazards")
    suspend fun getHighestId(): Long

    @Transaction
    @RawQuery
    suspend fun getFilteredHazards(
        sqLiteQuery: SupportSQLiteQuery
    ): List<HazardWithTraits>

    @Transaction
    @Query("SELECT * FROM hazards WHERE id is :id LIMIT 1")
    suspend fun getHazard(id: String): HazardWithTraits

    @Query("SELECT * FROM hazardTraits ORDER BY name")
    suspend fun getTraits(): List<HazardTrait>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertTraits(traits: List<HazardTrait>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertCrossRef(treasure: List<HazardsAndTraitsCrossRef>)

}