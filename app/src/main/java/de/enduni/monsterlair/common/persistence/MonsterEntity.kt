package de.enduni.monsterlair.common.persistence

import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverters
import de.enduni.monsterlair.common.persistence.database.EnumTypeConverters
import de.enduni.monsterlair.monsters.domain.MonsterType

@Entity(tableName = "monsters")
@TypeConverters(EnumTypeConverters::class)
data class MonsterEntity(
    @PrimaryKey val id: Long,
    val name: String,
    val url: String,
    val family: String,
    val level: Int,
    val alignment: String,
    val type: MonsterType,
    val size: String,
    val source: String
)