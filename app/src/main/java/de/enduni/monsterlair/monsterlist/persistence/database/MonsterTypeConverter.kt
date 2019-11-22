package de.enduni.monsterlair.monsterlist.persistence.database

import androidx.room.TypeConverter
import de.enduni.monsterlair.monsterlist.domain.MonsterType

class MonsterTypeConverter {
    @TypeConverter
    fun fromMonsterType(value: MonsterType): String {
        return value.toString()
    }

    @TypeConverter
    fun toMonsterType(value: String): MonsterType {
        return MonsterType.valueOf(value)
    }


}