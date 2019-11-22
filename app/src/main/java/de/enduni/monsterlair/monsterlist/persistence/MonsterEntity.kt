package de.enduni.monsterlair.monsterlist.persistence

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "monsters")
data class MonsterEntity(
    @PrimaryKey val name: String,
    val url: String,
    val family: String,
    val level: Int,
    val alignment: String,
    val type: String,
    val size: String
)