package de.enduni.monsterlair.common.persistence.database

import de.enduni.monsterlair.common.persistence.HazardEntity
import de.enduni.monsterlair.hazards.datasource.HazardDto
import de.enduni.monsterlair.hazards.domain.Hazard

class HazardEntityMapper {

    fun toEntity(dto: HazardDto) = HazardEntity(
        id = dto.id.toLong(),
        name = dto.name,
        url = dto.url,
        level = dto.level,
        complexity = dto.complexity,
        source = dto.source
    )

    fun toDomain(entity: HazardEntity) = Hazard(
        id = entity.id,
        name = entity.name,
        url = entity.url,
        level = entity.level,
        complexity = entity.complexity,
        source = entity.source
    )

}