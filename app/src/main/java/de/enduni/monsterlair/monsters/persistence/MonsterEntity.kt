package de.enduni.monsterlair.monsters.persistence

import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverters
import de.enduni.monsterlair.monsters.domain.MonsterType
import de.enduni.monsterlair.monsters.persistence.database.MonsterTypeConverter

@Entity(tableName = "monsters")
@TypeConverters(MonsterTypeConverter::class)
data class MonsterEntity(
    @PrimaryKey val name: String,
    val url: String,
    val family: String,
    val level: Int,
    val alignment: String,
    val type: MonsterType,
    val size: String
)