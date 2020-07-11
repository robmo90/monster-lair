package de.enduni.monsterlair.common.persistence.database

import androidx.room.TypeConverter
import de.enduni.monsterlair.common.domain.*
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

    @TypeConverter
    fun fromSource(value: Source): String {
        return value.toString()
    }

    @TypeConverter
    fun toSource(value: String): Source {
        return Source.valueOf(value)
    }

    @TypeConverter
    fun fromAlignment(value: Alignment): String {
        return value.toString()
    }

    @TypeConverter
    fun toAlignment(value: String): Alignment {
        return Alignment.valueOf(value)
    }

    @TypeConverter
    fun fromSize(value: Size): String {
        return value.toString()
    }

    @TypeConverter
    fun toSize(value: String): Size {
        return Size.valueOf(value)
    }

}