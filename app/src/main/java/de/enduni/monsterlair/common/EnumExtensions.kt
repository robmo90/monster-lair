package de.enduni.monsterlair.common

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import de.enduni.monsterlair.R
import de.enduni.monsterlair.common.domain.Complexity
import de.enduni.monsterlair.common.domain.MonsterType
import de.enduni.monsterlair.common.domain.TreasureCategory
import de.enduni.monsterlair.creator.view.DangerType
import de.enduni.monsterlair.encounters.domain.model.EncounterDifficulty
import de.enduni.monsterlair.encounters.domain.model.HazardRole
import de.enduni.monsterlair.encounters.domain.model.MonsterRole
import de.enduni.monsterlair.monsters.view.SortBy

@DrawableRes
fun MonsterType.getIcon(): Int {
    return when (this) {
        MonsterType.ABERRATION -> R.drawable.ic_monster_aberration
        MonsterType.ANIMAL -> R.drawable.ic_monster_animal
        MonsterType.BEAST -> R.drawable.ic_monster_beast
        MonsterType.DRAGON -> R.drawable.ic_monster_dragon
        MonsterType.ELEMENTAL -> R.drawable.ic_monster_elemental
        MonsterType.FIEND -> R.drawable.ic_monster_fiend
        MonsterType.GIANT -> R.drawable.ic_monster_giant
        MonsterType.HUMANOID -> R.drawable.ic_monster_humanoid
        MonsterType.MONITOR -> R.drawable.ic_monster_monitor
        MonsterType.OOZE -> R.drawable.ic_monster_ooze
        MonsterType.SPIRIT -> R.drawable.ic_monster_spirit
        MonsterType.UNDEAD -> R.drawable.ic_monster_undead
        MonsterType.ASTRAL -> R.drawable.ic_monster_astral
        MonsterType.CELESTIAL -> R.drawable.ic_monster_celestial
        MonsterType.CONSTRUCT -> R.drawable.ic_monster_construct
        MonsterType.ETHEREAL -> R.drawable.ic_monster_ethereal
        MonsterType.FEY -> R.drawable.ic_monster_fey
        MonsterType.FUNGUS -> R.drawable.ic_monster_fungus
        MonsterType.PLANT -> R.drawable.ic_monster_plant
        MonsterType.NONE -> R.drawable.ic_monster_ooze
    }
}

@StringRes
fun MonsterType.getStringRes(): Int {
    return when (this) {
        MonsterType.ABERRATION -> R.string.monster_type_aberration
        MonsterType.ANIMAL -> R.string.monster_type_animal
        MonsterType.BEAST -> R.string.monster_type_beast
        MonsterType.DRAGON -> R.string.monster_type_dragon
        MonsterType.ELEMENTAL -> R.string.monster_type_elemental
        MonsterType.FIEND -> R.string.monster_type_fiend
        MonsterType.GIANT -> R.string.monster_type_giant
        MonsterType.HUMANOID -> R.string.monster_type_humanoid
        MonsterType.MONITOR -> R.string.monster_type_monitor
        MonsterType.OOZE -> R.string.monster_type_ooze
        MonsterType.SPIRIT -> R.string.monster_type_spirit
        MonsterType.UNDEAD -> R.string.monster_type_undead
        MonsterType.ASTRAL -> R.string.monster_type_astral
        MonsterType.CELESTIAL -> R.string.monster_type_celestial
        MonsterType.CONSTRUCT -> R.string.monster_type_construct
        MonsterType.ETHEREAL -> R.string.monster_type_ethereal
        MonsterType.FEY -> R.string.monster_type_fey
        MonsterType.FUNGUS -> R.string.monster_type_fungus
        MonsterType.PLANT -> R.string.monster_type_plant
        MonsterType.NONE -> R.string.monster_type_none
    }
}

fun Complexity.getIcon(): Int {
    return when (this) {
        Complexity.SIMPLE -> R.drawable.ic_hazard_simple
        Complexity.COMPLEX -> R.drawable.ic_hazard_complex
    }
}

@StringRes
fun EncounterDifficulty.getStringRes(): Int {
    return when (this) {
        EncounterDifficulty.TRIVIAL -> R.string.difficulty_trivial
        EncounterDifficulty.LOW -> R.string.difficulty_low
        EncounterDifficulty.MODERATE -> R.string.difficulty_moderate
        EncounterDifficulty.SEVERE -> R.string.difficulty_severe
        EncounterDifficulty.EXTREME -> R.string.difficulty_extreme
    }
}

@StringRes
fun MonsterRole.getStringRes(): Int {
    return when (this) {
        MonsterRole.LOW_LACKEY -> R.string.role_low_lackey
        MonsterRole.MODERATE_LACKEY -> R.string.role_moderate_lackey
        MonsterRole.STANDARD_LACKEY -> R.string.role_standard_lackey
        MonsterRole.STANDARD_CREATURE -> R.string.role_standard_creature
        MonsterRole.LOW_BOSS -> R.string.role_low_boss
        MonsterRole.MODERATE_BOSS -> R.string.role_moderate_boss
        MonsterRole.SEVERE_BOSS -> R.string.role_severe_boss
        MonsterRole.EXTREME_BOSS -> R.string.role_extreme_boss
        MonsterRole.SOLO_BOSS -> R.string.role_solo_boss
        MonsterRole.TOO_HIGH -> R.string.role_too_high
        MonsterRole.TOO_LOW -> R.string.role_too_low
    }
}

@StringRes
fun Complexity.getStringRes(): Int {
    return when (this) {
        Complexity.SIMPLE -> R.string.hazard_complexity_simple
        Complexity.COMPLEX -> R.string.hazard_complexity_complex
    }
}

@StringRes
fun Complexity.getStringResForLabel(): Int {
    return when (this) {
        Complexity.SIMPLE -> R.string.hazard_filter_simple
        Complexity.COMPLEX -> R.string.hazard_filter_complex
    }
}

@StringRes
fun SortBy.getStringRes(): Int {
    return when (this) {
        SortBy.LEVEL -> R.string.monster_sort_by_level
        SortBy.NAME -> R.string.monster_sort_by_name
        SortBy.TYPE -> R.string.monster_sort_by_type
    }
}


@StringRes
fun HazardRole.getStringRes(): Int {
    return when (this) {
        HazardRole.LOW_LACKEY -> R.string.role_low_lackey_hazard
        HazardRole.MODERATE_LACKEY -> R.string.role_moderate_lackey_hazard
        HazardRole.STANDARD_LACKEY -> R.string.role_standard_lackey_hazard
        HazardRole.STANDARD_CREATURE -> R.string.role_standard_hazard
        HazardRole.LOW_BOSS -> R.string.role_low_boss_hazard
        HazardRole.MODERATE_BOSS -> R.string.role_moderate_boss_hazard
        HazardRole.SEVERE_BOSS -> R.string.role_severe_boss_hazard
        HazardRole.EXTREME_BOSS -> R.string.role_extreme_boss_hazard
        HazardRole.SOLO_BOSS -> R.string.role_solo_boss_hazard
        HazardRole.TOO_HIGH -> R.string.role_too_high
        HazardRole.TOO_LOW -> R.string.role_too_low
    }
}


fun HazardRole.getXp(complexity: Complexity): Int {
    return when (complexity) {
        Complexity.SIMPLE -> xpSimple
        Complexity.COMPLEX -> xpComplex
    }
}

@StringRes
fun DangerType.getStringRes(): Int {
    return when (this) {
        DangerType.MONSTER -> R.string.danger_type_monster
        DangerType.HAZARD -> R.string.danger_type_hazards
    }
}

fun EncounterDifficulty.getDefaultMaxLevel(): Int {
    return when (this) {
        EncounterDifficulty.TRIVIAL -> 0
        EncounterDifficulty.LOW -> 1
        EncounterDifficulty.MODERATE -> 2
        EncounterDifficulty.SEVERE -> 3
        EncounterDifficulty.EXTREME -> 4
    }
}

@DrawableRes
fun TreasureCategory.getIcon(): Int {
    return when (this) {
        TreasureCategory.ADVENTURING_GEAR -> R.drawable.ic_adventuring_gear
        TreasureCategory.ALCHEMICAL_BOMBS -> R.drawable.ic_alchemical_bomb
        TreasureCategory.ALCHEMICAL_ELIXIRS -> R.drawable.ic_alchemical_elixir
        TreasureCategory.ALCHEMICAL_POISONS -> R.drawable.ic_alchemical_poison
        TreasureCategory.ALCHEMICAL_TOOLS -> R.drawable.ic_alchemical_tools
        TreasureCategory.DRUGS -> R.drawable.ic_drugs
        TreasureCategory.ARMOR_MAGIC -> R.drawable.ic_magic_armor
        TreasureCategory.ARMOR_PRECIOUS -> R.drawable.ic_magic_armor_precious
        TreasureCategory.ARMOR_SPECIFIC -> R.drawable.ic_magic_armor_specific
        TreasureCategory.ARTIFACTS -> R.drawable.ic_artifacts
        TreasureCategory.AMMUNITION -> R.drawable.ic_ammunition
        TreasureCategory.OILS -> R.drawable.ic_oils
        TreasureCategory.OTHER_CONSUMABLES -> R.drawable.ic_other_consumables
        TreasureCategory.POTIONS -> R.drawable.ic_potions
        TreasureCategory.SCROLLS -> R.drawable.ic_scrolls
        TreasureCategory.TALISMANS -> R.drawable.ic_talismans
        TreasureCategory.CURSED_ITEMS -> R.drawable.ic_cursed_items
        TreasureCategory.HELD_ITEMS -> R.drawable.ic_held_items
        TreasureCategory.INTELLIGENT_ITEMS -> R.drawable.ic_intelligent_items
        TreasureCategory.MATERIALS -> R.drawable.ic_materials
        TreasureCategory.RUNES_ARMOR_PROPERTY -> R.drawable.ic_armor_runes
        TreasureCategory.RUNES_ARMOR_FUNDAMENTAL -> R.drawable.ic_armor_runes
        TreasureCategory.RUNES_WEAPON_PROPERTY -> R.drawable.ic_weapon_runes
        TreasureCategory.RUNES_WEAPON_FUNDAMENTAL -> R.drawable.ic_weapon_runes
        TreasureCategory.SERVICES -> R.drawable.ic_services
        TreasureCategory.SHIELDS_PRECIOUS -> R.drawable.ic_shield_precious
        TreasureCategory.SHIELDS_SPECIFIC -> R.drawable.ic_shield_specific
        TreasureCategory.SNARES -> R.drawable.ic_snares
        TreasureCategory.STAVES -> R.drawable.ic_staves
        TreasureCategory.STRUCTURES -> R.drawable.ic_structures
        TreasureCategory.TATTOOS -> R.drawable.ic_tattoos
        TreasureCategory.WANDS_MAGIC -> R.drawable.ic_magic_wands
        TreasureCategory.WANDS_SPECIALTY -> R.drawable.ic_specialty_wands
        TreasureCategory.WEAPONS_MAGIC -> R.drawable.ic_magic_weapons
        TreasureCategory.WEAPONS_PRECIOUS -> R.drawable.ic_precious_weapons
        TreasureCategory.WEAPONS_SPECIFIC -> R.drawable.ic_specific_weapons
        TreasureCategory.WORN_APEX -> R.drawable.ic_worn_apex
        TreasureCategory.WORN_COMPANION -> R.drawable.ic_worn_companion
        TreasureCategory.WORN_OTHER -> R.drawable.ic_worn_other
    }
}
