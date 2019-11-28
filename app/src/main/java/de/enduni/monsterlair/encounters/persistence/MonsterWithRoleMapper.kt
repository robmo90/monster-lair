package de.enduni.monsterlair.encounters.persistence

import de.enduni.monsterlair.common.persistence.MonsterEntity
import de.enduni.monsterlair.encounters.domain.model.MonsterRole
import de.enduni.monsterlair.encounters.domain.model.MonsterWithRole
import de.enduni.monsterlair.monsters.domain.Monster

class MonsterWithRoleMapper {

    fun mapToMonsterWithRole(monster: MonsterEntity, encounterLevel: Int): MonsterWithRole {
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
                source = it.source,
                role = it.level.determineRole(encounterLevel)
            )
        }
    }

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
                source = it.source,
                role = it.level.determineRole(encounterLevel)
            )
        }
    }

    private fun Int.determineRole(encounterLevel: Int): MonsterRole {
        val normalizedLevel = this - encounterLevel
        return if (normalizedLevel <= -5) {
            MonsterRole.TOO_LOW
        } else {
            when (normalizedLevel) {
                -4 -> MonsterRole.LOW_LACKEY
                -3 -> MonsterRole.MODERATE_LACKEY
                -2 -> MonsterRole.STANDARD_LACKEY
                -1 -> MonsterRole.STANDARD_CREATURE
                0 -> MonsterRole.LOW_BOSS
                1 -> MonsterRole.MODERATE_BOSS
                2 -> MonsterRole.SEVERE_BOSS
                3 -> MonsterRole.EXTREME_BOSS
                4 -> MonsterRole.SOLO_BOSS
                else -> MonsterRole.TOO_HIGH
            }
        }
    }

}