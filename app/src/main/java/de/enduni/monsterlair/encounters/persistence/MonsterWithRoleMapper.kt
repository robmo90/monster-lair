package de.enduni.monsterlair.encounters.persistence

import de.enduni.monsterlair.common.domain.Strength
import de.enduni.monsterlair.common.persistence.MonsterEntity
import de.enduni.monsterlair.common.persistence.MonsterTrait
import de.enduni.monsterlair.encounters.domain.model.MonsterRole
import de.enduni.monsterlair.encounters.domain.model.MonsterWithRole
import de.enduni.monsterlair.monsters.domain.Monster

class MonsterWithRoleMapper {

    fun mapToMonsterWithRole(
        monster: MonsterEntity,
        traits: List<MonsterTrait>,
        encounterLevel: Int,
        withoutProficiency: Boolean
    ): MonsterWithRole {
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
                sourceType = it.sourceType,
                rarity = it.rarity,
                traits = traits.map { it.name },
                description = it.description,
                role = MonsterRole.determineRole(
                    it.level,
                    encounterLevel,
                    Strength.STANDARD,
                    withoutProficiency
                )
            )
        }
    }

    fun mapToMonsterWithRole(
        monster: Monster,
        encounterLevel: Int,
        withoutProficiency: Boolean
    ): MonsterWithRole {
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
                sourceType = it.sourceType,
                rarity = it.rarity,
                traits = it.traits,
                description = it.description,
                role = MonsterRole.determineRole(
                    it.level,
                    encounterLevel,
                    Strength.STANDARD,
                    withoutProficiency
                )
            )
        }
    }

}