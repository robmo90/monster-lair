package de.enduni.monsterlair.common.persistence.database

import androidx.room.TypeConverter
import de.enduni.monsterlair.common.domain.Complexity
import de.enduni.monsterlair.common.domain.MonsterType
import de.enduni.monsterlair.encounters.domain.model.EncounterDifficulty

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

    @TypeConverter
    fun fromComplexity(value: Complexity): String {
        return value.toString()
    }

    @TypeConverter
    fun toComplexity(value: String): Complexity {
        return Complexity.valueOf(value)
    }

}