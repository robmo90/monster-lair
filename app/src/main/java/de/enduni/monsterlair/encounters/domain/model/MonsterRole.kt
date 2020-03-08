package de.enduni.monsterlair.encounters.domain.model

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
    TOO_HIGH(240)
}