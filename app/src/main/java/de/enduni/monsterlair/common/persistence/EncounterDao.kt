package de.enduni.monsterlair.common.persistence

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface EncounterDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertEncounter(encounter: EncounterEntity): Long

    @Insert
    suspend fun insertMonstersForEncounter(monstersForEncounter: List<MonsterForEncounterEntity>)

    @Insert
    suspend fun insertHazardsForEncounter(hazardsForEncounter: List<HazardForEncounterEntity>)

    @Query("SELECT * FROM encounters ORDER BY id DESC")
    fun getEncounterFlow(): Flow<List<EncounterEntity>>

    @Query("SELECT * FROM encounters ORDER BY id DESC")
    fun getEncounters(): List<EncounterEntity>

    @Query("SELECT * FROM encounters WHERE id is :id LIMIT 1")
    suspend fun getEncounter(id: Long): EncounterEntity

    @Query("SELECT * FROM monsters_for_encounters WHERE encounter_id = :encounterId")
    suspend fun getAllMonstersForEncounter(encounterId: Long): List<MonsterForEncounterEntity>

    @Query("SELECT * FROM hazards_for_encounters WHERE encounter_id = :encounterId")
    suspend fun getAllHazardsForEncounter(encounterId: Long): List<HazardForEncounterEntity>

    @Query("DELETE FROM encounters WHERE id = :id")
    suspend fun deleteEncounter(id: Long)


}