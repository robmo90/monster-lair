package de.enduni.monsterlair.monsters.persistence

import de.enduni.monsterlair.common.datasource.datasource.MonsterDto
import de.enduni.monsterlair.common.persistence.MonsterEntity
import de.enduni.monsterlair.monsters.domain.Monster

class MonsterEntityMapper {

    fun toEntity(dto: MonsterDto) = MonsterEntity(
        id = dto.id.toLong(),
        name = dto.name,
        url = dto.url,
        level = dto.level,
        type = dto.type,
        family = dto.family,
        alignment = dto.alignment,
        size = dto.size,
        source = dto.source
    )

    fun toModel(entity: MonsterEntity) = Monster(
        id = entity.id,
        name = entity.name,
        url = entity.url,
        level = entity.level,
        type = entity.type,
        family = entity.family,
        alignment = entity.alignment,
        size = entity.size,
        source = entity.source
    )

    fun toEntity(dto: Monster, id: Long) = MonsterEntity(
        id = id,
        name = dto.name,
        url = dto.url,
        level = dto.level,
        type = dto.type,
        family = dto.family,
        alignment = dto.alignment,
        size = dto.size,
        source = dto.source
    )

}