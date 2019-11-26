package de.enduni.monsterlair.encounters.persistence

import de.enduni.monsterlair.common.persistence.MonsterEntity
import de.enduni.monsterlair.encounters.domain.model.CreatureRole
import de.enduni.monsterlair.encounters.domain.model.MonsterWithRole

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
                role = it.determineRole(encounterLevel)
            )
        }
    }

    private fun MonsterEntity.determineRole(encounterLevel: Int): CreatureRole {
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