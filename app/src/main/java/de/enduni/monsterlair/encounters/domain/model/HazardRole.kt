package de.enduni.monsterlair.encounters.domain.model

enum class HazardRole(val xpSimple: Int, val xpComplex: Int) {
    TOO_LOW(1, 5),
    LOW_LACKEY(2, 10),
    MODERATE_LACKEY(3, 15),
    STANDARD_LACKEY(4, 20),
    STANDARD_CREATURE(6, 30),
    LOW_BOSS(8, 40),
    MODERATE_BOSS(12, 60),
    SEVERE_BOSS(16, 80),
    EXTREME_BOSS(24, 120),
    SOLO_BOSS(32, 160),
    TOO_HIGH(48, 240);
}