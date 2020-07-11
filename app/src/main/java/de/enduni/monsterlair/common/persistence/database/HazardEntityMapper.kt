package de.enduni.monsterlair.common.persistence.database

import de.enduni.monsterlair.common.datasource.hazard.HazardDto
import de.enduni.monsterlair.common.persistence.HazardEntity
import de.enduni.monsterlair.hazards.domain.Hazard

class HazardEntityMapper {

    fun toEntity(dto: HazardDto) = HazardEntity(
        id = dto.id,
        name = dto.name,
        url = dto.url,
        level = dto.level,
        complexity = dto.complexity,
        rarity = dto.rarity,
        source = dto.source,
        sourceType = dto.sourceType
    )

    fun toDomain(entity: HazardEntity) = Hazard(
        id = entity.id,
        name = entity.name,
        url = entity.url,
        level = entity.level,
        rarity = entity.rarity,
        complexity = entity.complexity,
        source = entity.source,
        sourceType = entity.sourceType,
        traits = emptyList()
    )

}
