package de.enduni.monsterlair.common.persistence.database

import androidx.room.TypeConverter
import de.enduni.monsterlair.monsters.domain.MonsterType

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