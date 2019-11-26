package de.enduni.monsterlair.encounters.domain

import de.enduni.monsterlair.monsters.domain.Monster

class MonsterWithRoleMapper {

    fun mapToMonsterWithRole(monster: Monster, encounterLevel: Int): MonsterWithRole {
        return monster.let {
            MonsterWithRole(
                id = it.id,
                name = it.name,
                url = it.url,
                family = it.family,
                level = it.level,
                alignment = it.alignment,
                type = it.type,
                size = it.size,
                role = it.determineRole(encounterLevel)
            )
        }
    }

    private fun Monster.determineRole(encounterLevel: Int): CreatureRole {
        val normalizedLevel = this.level - encounterLevel
        return if (normalizedLevel <= -5) {
            CreatureRole.TOO_LOW
        } else {
            when (normalizedLevel) {
                -4 -> CreatureRole.LOW_LACKEY
                -3 -> CreatureRole.MODERATE_LACKEY
                -2 -> CreatureRole.STANDARD_LACKEY
                -1 -> CreatureRole.STANDARD_CREATURE
                0 -> CreatureRole.LOW_BOSS
                1 -> CreatureRole.MODERATE_BOSS
                2 -> CreatureRole.SEVERE_BOSS
                3 -> CreatureRole.EXTREME_BOSS
                4 -> CreatureRole.SOLO_BOSS
                else -> CreatureRole.TOO_HIGH
            }
        }
    }

}