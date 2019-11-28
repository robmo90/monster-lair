package de.enduni.monsterlair.encounters.persistence

import de.enduni.monsterlair.common.persistence.HazardEntity
import de.enduni.monsterlair.encounters.domain.model.HazardRole
import de.enduni.monsterlair.encounters.domain.model.HazardWithRole
import de.enduni.monsterlair.hazards.domain.Hazard

class HazardWithRoleMapper {

    fun mapToHazardWithRole(hazard: HazardEntity, encounterLevel: Int): HazardWithRole {
        return hazard.let {
            HazardWithRole(
                id = it.id,
                name = it.name,
                url = it.url,
                level = it.level,
                complexity = it.complexity,
                source = it.source,
                role = it.level.determineRole(encounterLevel)
            )
        }
    }

    fun mapToHazardWithRole(hazard: Hazard, encounterLevel: Int): HazardWithRole {
        return hazard.let {
            HazardWithRole(
                id = it.id,
                name = it.name,
                url = it.url,
                level = it.level,
                complexity = it.complexity,
                source = it.source,
                role = it.level.determineRole(encounterLevel)
            )
        }
    }

    private fun Int.determineRole(encounterLevel: Int): HazardRole {
        val normalizedLevel = this - encounterLevel
        return if (normalizedLevel <= -5) {
            HazardRole.TOO_LOW
        } else {
            when (normalizedLevel) {
                -4 -> HazardRole.LOW_LACKEY
                -3 -> HazardRole.MODERATE_LACKEY
                -2 -> HazardRole.STANDARD_LACKEY
                -1 -> HazardRole.STANDARD_CREATURE
                0 -> HazardRole.LOW_BOSS
                1 -> HazardRole.MODERATE_BOSS
                2 -> HazardRole.SEVERE_BOSS
                3 -> HazardRole.EXTREME_BOSS
                4 -> HazardRole.SOLO_BOSS
                else -> HazardRole.TOO_HIGH
            }
        }
    }

}