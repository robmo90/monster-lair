package de.enduni.monsterlair.common.persistence.database

import androidx.room.TypeConverter
import de.enduni.monsterlair.encounters.domain.EncounterDifficulty
import de.enduni.monsterlair.monsters.domain.MonsterType

class EnumTypeConverters {
    @TypeConverter
    fun fromMonsterType(value: MonsterType): String {
        return value.toString()
    }

    @TypeConverter
    fun toMonsterType(value: String): MonsterType {
        return MonsterType.valueOf(value)
    }

    @TypeConverter
    fun fromDifficulty(value: EncounterDifficulty): String {
        return value.toString()
    }

    @TypeConverter
    fun toDifficulty(value: String): EncounterDifficulty {
        return EncounterDifficulty.valueOf(value)
    }

}