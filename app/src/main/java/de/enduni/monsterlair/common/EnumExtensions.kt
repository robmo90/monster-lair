package de.enduni.monsterlair.common

import androidx.annotation.StringRes
import de.enduni.monsterlair.R
import de.enduni.monsterlair.encounters.monsters.domain.CreatureRole
import de.enduni.monsterlair.monsters.domain.MonsterType

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
fun CreatureRole.getStringRes(): Int {
    return when (this) {
        CreatureRole.LOW_LACKEY -> R.string.role_low_lackey
        CreatureRole.MODERATE_LACKEY -> R.string.role_moderate_lackey
        CreatureRole.STANDARD_LACKEY -> R.string.role_standard_lackey
        CreatureRole.STANDARD_CREATURE -> R.string.role_standard_creature
        CreatureRole.LOW_BOSS -> R.string.role_low_boss
        CreatureRole.MODERATE_BOSS -> R.string.role_moderate_boss
        CreatureRole.SEVERE_BOSS -> R.string.role_severe_boss
        CreatureRole.EXTREME_BOSS -> R.string.role_extreme_boss
        CreatureRole.SOLO_BOSS -> R.string.role_solo_boss
        CreatureRole.TOO_HIGH -> R.string.role_too_high
        CreatureRole.TOO_LOW -> R.string.role_too_low
    }
}