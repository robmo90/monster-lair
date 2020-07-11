package de.enduni.monsterlair.common.persistence

import androidx.room.*
import androidx.sqlite.db.SupportSQLiteQuery
import kotlinx.coroutines.flow.Flow

@Dao
interface HazardDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertHazards(hazards: List<HazardEntity>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertHazard(hazard: HazardEntity)

    @Query("SELECT * FROM hazards ORDER BY name")
    suspend fun getAllHazards(): List<HazardEntity>

    @Query("SELECT MAX(id) FROM hazards")
    suspend fun getHighestId(): Long

    @RawQuery
    suspend fun getFilteredHazards(
        sqLiteQuery: SupportSQLiteQuery
    ): List<HazardEntity>

    @RawQuery(observedEntities = [HazardEntity::class])
    fun getFilteredHazardFlow(
        sqLiteQuery: SupportSQLiteQuery
    ): Flow<List<HazardEntity>>


    @Query("SELECT * FROM hazards WHERE id is :id LIMIT 1")
    suspend fun getHazard(id: String): HazardEntity

}