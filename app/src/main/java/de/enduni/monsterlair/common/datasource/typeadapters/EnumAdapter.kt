package de.enduni.monsterlair.common.datasource.typeadapters

import com.squareup.moshi.FromJson
import de.enduni.monsterlair.common.domain.*
import java.util.*

class EnumAdapter {
    @FromJson
    fun fromJson(string: String): MonsterType {
        val type = MonsterType.values().find { it.toString() == string }
        return type ?: MonsterType.NONE
    }

    @FromJson
    fun sizeFromJson(string: String): Size {
        return Size.values()
            .find { it.toString() == string.toUpperCase(Locale.ROOT) } ?: Size.MEDIUM
    }

    @FromJson
    fun rarityFromJson(string: String): Rarity {
        return Rarity.values()
            .find { it.toString() == string.toUpperCase(Locale.ROOT) } ?: Rarity.COMMON
    }

    @FromJson
    fun sourceFromJson(string: String): Source {
        return Source.values()
            .find { it.toString() == string } ?: Source.MISC
    }

    @FromJson
    fun alignmentFromJson(string: String): Alignment {
        return when (string) {
            "Neutral Evil" -> Alignment.NEUTRAL_EVIL
            "Neutral Good" -> Alignment.NEUTRAL_GOOD
            "Lawful Evil" -> Alignment.LAWFUL_EVIL
            "Lawful Good" -> Alignment.LAWFUL_GOOD
            "Chaotic Good" -> Alignment.CHAOTIC_GOOD
            "Chaotic Evil" -> Alignment.CHAOTIC_GOOD
            "Lawful Neutral" -> Alignment.LAWFUL_NEUTRAL
            "Chaotic Neutral" -> Alignment.CHAOTIC_NEUTRAL
            else -> Alignment.NEUTRAL

        }
    }

    @FromJson
    fun treasureCategoryFromJson(string: String): TreasureCategory {
        return string.toUpperCase(Locale.ROOT)
            .replace("-", "_")
            .let { TreasureCategory.valueOf(it) }
    }
}