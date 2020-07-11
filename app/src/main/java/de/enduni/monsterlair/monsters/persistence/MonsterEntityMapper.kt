package de.enduni.monsterlair.monsters.persistence

import de.enduni.monsterlair.common.datasource.datasource.MonsterDto
import de.enduni.monsterlair.common.persistence.MonsterEntity
import de.enduni.monsterlair.monsters.domain.Monster

class MonsterEntityMapper {

    fun toEntity(dto: MonsterDto) = MonsterEntity(
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
    )

    fun toModel(entity: MonsterEntity) = Monster(
        id = entity.id,
        name = entity.name,
        url = entity.url,
        level = entity.level,
        type = entity.type,
        family = entity.family,
        rarity = entity.rarity,
        alignment = entity.alignment,
        size = entity.size,
        source = entity.source,
        sourceType = entity.sourceType,
        traits = emptyList()
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