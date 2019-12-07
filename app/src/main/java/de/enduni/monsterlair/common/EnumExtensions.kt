package de.enduni.monsterlair.common

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import de.enduni.monsterlair.R
import de.enduni.monsterlair.common.domain.Complexity
import de.enduni.monsterlair.common.domain.MonsterType
import de.enduni.monsterlair.encounters.domain.model.EncounterDifficulty
import de.enduni.monsterlair.encounters.domain.model.HazardRole
import de.enduni.monsterlair.encounters.domain.model.MonsterRole
import de.enduni.monsterlair.hazards.view.HazardType
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
fun HazardType.getStringRes(): Int {
    return when (this) {
        HazardType.COMPLEX -> R.string.hazard_filter_complex
        HazardType.SIMPLE -> R.string.hazard_filter_simple
        HazardType.ALL -> R.string.hazard_filter_all
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