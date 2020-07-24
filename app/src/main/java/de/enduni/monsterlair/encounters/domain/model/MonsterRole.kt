package de.enduni.monsterlair.encounters.domain.model

import de.enduni.monsterlair.common.domain.Strength

enum class MonsterRole(val xp: Int) {
    TOO_LOW(5),
    LOW_LACKEY(10),
    MODERATE_LACKEY(15),
    STANDARD_LACKEY(20),
    STANDARD_CREATURE(30),
    LOW_BOSS(40),
    MODERATE_BOSS(60),
    SEVERE_BOSS(80),
    EXTREME_BOSS(120),
    SOLO_BOSS(160),
    TOO_HIGH(240),


    WITHOUT_LEVEL_LOW_LACKEY(9),
    WITHOUT_LEVEL_WEAK_LACKEY(12),
    WITHOUT_LEVEL_MODERATE_LACKEY(14),
    WITHOUT_LEVEL_STRONGER_LACKEY(18),
    WITHOUT_LEVEL_STANDARD_LACKEY(21),
    WITHOUT_LEVEL_WEAKER_STANDARD_CREATURE(26),
    WITHOUT_LEVEL_STANDARD_CREATURE(32),
    WITHOUT_LEVEL_LOW_BOSS(40),
    WITHOUT_LEVEL_WEAK_BOSS(48),
    WITHOUT_LEVEL_MODERATE_BOSS(60),
    WITHOUT_LEVEL_STRONGER_BOSS(72),
    WITHOUT_LEVEL_SEVERE_BOSS(90),
    WITHOUT_LEVEL_EXTREME_BOSS(108),
    WITHOUT_LEVEL_MORE_EXTREME_BOSS(135),
    WITHOUT_LEVEL_SOLO_BOSS(160);


    companion object {

        fun determineRole(
            monsterLevel: Int,
            encounterLevel: Int,
            strength: Strength,
            withoutProficiency: Boolean
        ): MonsterRole {
            val normalizedLevel = monsterLevel - encounterLevel + strength.levelAdjustment
            return if (withoutProficiency) {
                getRoleWithoutProficiency(normalizedLevel)
            } else {
                getRole(normalizedLevel)
            }

        }

        private fun getRole(normalizedLevel: Int): MonsterRole {
            return if (normalizedLevel <= -5) {
                TOO_LOW
            } else {
                when (normalizedLevel) {
                    -4 -> LOW_LACKEY
                    -3 -> MODERATE_LACKEY
                    -2 -> STANDARD_LACKEY
                    -1 -> STANDARD_CREATURE
                    0 -> LOW_BOSS
                    1 -> MODERATE_BOSS
                    2 -> SEVERE_BOSS
                    3 -> EXTREME_BOSS
                    4 -> SOLO_BOSS
                    else -> TOO_HIGH
                }
            }
        }

        private fun getRoleWithoutProficiency(normalizedLevel: Int): MonsterRole {
            return if (normalizedLevel <= -8) {
                TOO_LOW
            } else {
                when (normalizedLevel) {
                    -7 -> WITHOUT_LEVEL_LOW_LACKEY
                    -6 -> WITHOUT_LEVEL_WEAK_LACKEY
                    -5 -> WITHOUT_LEVEL_MODERATE_LACKEY
                    -4 -> WITHOUT_LEVEL_STRONGER_LACKEY
                    -3 -> WITHOUT_LEVEL_STANDARD_LACKEY
                    -2 -> WITHOUT_LEVEL_WEAKER_STANDARD_CREATURE
                    -1 -> WITHOUT_LEVEL_STANDARD_CREATURE
                    0 -> WITHOUT_LEVEL_LOW_BOSS
                    1 -> WITHOUT_LEVEL_WEAK_BOSS
                    2 -> WITHOUT_LEVEL_MODERATE_BOSS
                    3 -> WITHOUT_LEVEL_STRONGER_BOSS
                    4 -> WITHOUT_LEVEL_SEVERE_BOSS
                    5 -> WITHOUT_LEVEL_EXTREME_BOSS
                    6 -> WITHOUT_LEVEL_MORE_EXTREME_BOSS
                    7 -> WITHOUT_LEVEL_SOLO_BOSS
                    else -> TOO_HIGH
                }
            }
        }


    }

}