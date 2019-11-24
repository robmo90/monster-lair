package de.enduni.monsterlair.monsters.datasource

import de.enduni.monsterlair.monsters.domain.Monster
import de.enduni.monsterlair.monsters.persistence.MonsterEntity

class MonsterEntityMapper {

    fun toEntity(dto: MonsterDto) = MonsterEntity(
        name = dto.name,
        url = dto.url,
        level = dto.level,
        type = dto.type,
        family = dto.family,
        alignment = dto.alignment,
        size = dto.size
    )

    fun toModel(entity: MonsterEntity) = Monster(
        name = entity.name,
        url = entity.url,
        level = entity.level,
        type = entity.type,
        family = entity.family,
        alignment = entity.alignment,
        size = entity.size
    )

}