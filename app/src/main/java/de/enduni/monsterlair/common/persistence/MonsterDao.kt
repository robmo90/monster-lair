package de.enduni.monsterlair.common.persistence

import androidx.room.*
import androidx.sqlite.db.SupportSQLiteQuery
import kotlinx.coroutines.flow.Flow

@Dao
interface MonsterDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertMonsters(monsters: List<MonsterEntity>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertMonster(monster: MonsterEntity)

    @Query("DELETE FROM monsters WHERE id = :id")
    suspend fun deleteMonster(id: String)

    @Query("SELECT * FROM monsters ORDER BY name")
    suspend fun getAllMonsters(): List<MonsterEntity>

    @Query("SELECT MAX(id) FROM monsters")
    suspend fun getHighestId(): Long

    @Transaction
    @RawQuery
    suspend fun getFilteredMonsters(
        sqLiteQuery: SupportSQLiteQuery
    ): List<MonsterWithTraits>

    @RawQuery(observedEntities = [MonsterEntity::class])
    fun getFilteredMonsterFlow(
        sqLiteQuery: SupportSQLiteQuery
    ): Flow<List<MonsterEntity>>

    @Transaction
    @Query("SELECT * FROM monsters WHERE id is :id LIMIT 1")
    suspend fun getMonster(id: String): MonsterWithTraits

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertTraits(traits: List<MonsterTrait>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertCrossRef(crossRefs: List<MonsterAndTraitsCrossRef>)

    @Query("SELECT * FROM monsterTraits ORDER BY name")
    suspend fun getTraits(): List<MonsterTrait>

}