package de.enduni.monsterlair.common.persistence

import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverters
import de.enduni.monsterlair.common.persistence.database.EnumTypeConverters
import de.enduni.monsterlair.hazards.domain.Complexity

@Entity(tableName = "hazards")
@TypeConverters(EnumTypeConverters::class)
data class HazardEntity(
    @PrimaryKey val id: Long,
    val name: String,
    val url: String,
    val level: Int,
    val complexity: Complexity,
    val source: String
)