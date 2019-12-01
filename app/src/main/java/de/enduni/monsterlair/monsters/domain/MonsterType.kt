package de.enduni.monsterlair.monsters.domain

import androidx.annotation.Keep
import com.squareup.moshi.FromJson

@Keep
enum class MonsterType {
    ABERRATION,
    ANIMAL,
    ASTRAL,
    BEAST,
    CELESTIAL,
    CONSTRUCT,
    DRAGON,
    ELEMENTAL,
    ETHEREAL,
    FEY,
    FIEND,
    FUNGUS,
    GIANT,
    HUMANOID,
    MONITOR,
    OOZE,
    PLANT,
    SPIRIT,
    UNDEAD,
    NONE;
}

class MonsterTypeAdapter {
    @FromJson
    fun fromJson(string: String): MonsterType {
        val type = MonsterType.values().find { it.toString() == string }
        return type ?: MonsterType.NONE
    }
}