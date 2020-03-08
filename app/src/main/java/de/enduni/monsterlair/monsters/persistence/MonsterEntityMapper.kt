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
        url = entity.url.nullIfEmpty(),
        level = entity.level,
        type = entity.type,
        family = entity.family,
        alignment = entity.alignment.nullIfEmpty(),
        size = entity.size.nullIfEmpty(),
        source = entity.source
    )

    fun toEntity(dto: Monster, id: Long) = MonsterEntity(
        id = id,
        name = dto.name,
        url = dto.url ?: "",
        level = dto.level,
        type = dto.type,
        family = dto.family,
        alignment = dto.alignment ?: "",
        size = dto.size ?: "",
        source = dto.source
    )

    private fun String.nullIfEmpty(): String? {
        return if (this.isBlank()) {
            null
        } else {
            this
        }
    }


}