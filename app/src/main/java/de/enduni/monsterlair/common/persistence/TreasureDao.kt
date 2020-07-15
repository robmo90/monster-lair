package de.enduni.monsterlair.common.persistence

import androidx.room.*

@Dao
interface TreasureDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertTreasure(treasure: List<TreasureEntity>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertTreasure(treasure: TreasureEntity)

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertTraits(treasure: List<TreasureTrait>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertCrossRef(treasure: List<TreasureAndTraitsCrossRef>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun saveTreasure(treasure: TreasureEntity)

    @Transaction
    @Query("SELECT * FROM treasures ORDER BY name")
    suspend fun getTreasure(): List<TreasureWithTraits>

    @Query("SELECT * FROM treasures WHERE id is :id LIMIT 1")
    suspend fun getTreasure(id: String): TreasureWithTraits

}