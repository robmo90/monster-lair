package de.enduni.monsterlair.monsters.persistence

import de.enduni.monsterlair.common.datasource.monsters.MonsterDto
import de.enduni.monsterlair.common.persistence.MonsterEntity
import de.enduni.monsterlair.common.persistence.MonsterTrait
import de.enduni.monsterlair.common.persistence.MonsterWithTraits
import de.enduni.monsterlair.monsters.domain.Monster

class MonsterEntityMapper {

    fun toEntity(dto: MonsterDto) = Pair(
        MonsterEntity(
            id = dto.id,
            name = dto.name,
            url = dto.url,
            level = dto.level,
            type = dto.type,
            family = dto.family,
            alignment = dto.alignment,
            rarity = dto.rarity,
            size = dto.size,
            source = dto.source,
            sourceType = dto.sourceType
        ),
        dto.traits.map { MonsterTrait(it) }
    )

    fun toModel(entity: MonsterWithTraits) = Monster(
        id = entity.monster.id,
        name = entity.monster.name,
        url = entity.monster.url,
        level = entity.monster.level,
        type = entity.monster.type,
        family = entity.monster.family,
        rarity = entity.monster.rarity,
        alignment = entity.monster.alignment,
        size = entity.monster.size,
        source = entity.monster.source,
        sourceType = entity.monster.sourceType,
        traits = entity.traits.map { it.name }
    )

    fun toEntity(dto: Monster) = MonsterEntity(
        id = dto.id,
        name = dto.name,
        url = dto.url ?: "",
        level = dto.level,
        type = dto.type,
        family = dto.family,
        alignment = dto.alignment,
        size = dto.size,
        rarity = dto.rarity,
        source = dto.source,
        sourceType = dto.sourceType
    )

}