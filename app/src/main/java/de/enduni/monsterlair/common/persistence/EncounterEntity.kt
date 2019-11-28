package de.enduni.monsterlair.common.persistence

import androidx.room.*
import de.enduni.monsterlair.common.persistence.database.EnumTypeConverters
import de.enduni.monsterlair.encounters.domain.model.EncounterDifficulty

@Entity(tableName = "encounters")
@TypeConverters(EnumTypeConverters::class)
data class EncounterEntity(
    @PrimaryKey val id: Long? = null,
    val level: Int,
    val numberOfPlayers: Int,
    val name: String,
    val difficulty: EncounterDifficulty
)

@Entity(
    tableName = "monsters_for_encounters",
    indices = [Index(
        value = ["encounter_id"],
        name = "monster_for_encounter_index"
    )],
    foreignKeys = [ForeignKey(
        entity = EncounterEntity::class,
        parentColumns = ["id"],
        childColumns = ["encounter_id"],
        onDelete = ForeignKey.CASCADE
    )]
)
data class MonsterForEncounterEntity(
    @PrimaryKey val id: Long? = null,
    val monsterId: Long,
    val count: Int,
    @ColumnInfo(name = "encounter_id") val encounterId: Long
)

@Entity(
    tableName = "hazards_for_encounters",
    indices = [Index(
        value = ["encounter_id"],
        name = "hazard_for_encounter_index"
    )],
    foreignKeys = [ForeignKey(
        entity = EncounterEntity::class,
        parentColumns = ["id"],
        childColumns = ["encounter_id"],
        onDelete = ForeignKey.CASCADE
    )]
)
data class HazardForEncounterEntity(
    @PrimaryKey val id: Long? = null,
    val hazardId: Long,
    val count: Int,
    @ColumnInfo(name = "encounter_id") val encounterId: Long
)