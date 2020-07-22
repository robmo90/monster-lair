package de.enduni.monsterlair.common.persistence.database

import de.enduni.monsterlair.common.datasource.hazard.HazardDto
import de.enduni.monsterlair.common.persistence.HazardEntity
import de.enduni.monsterlair.common.persistence.HazardTrait
import de.enduni.monsterlair.common.persistence.HazardWithTraits
import de.enduni.monsterlair.hazards.domain.Hazard

class HazardEntityMapper {

    fun fromDtoToEntity(dto: HazardDto) = Pair(
        HazardEntity(
            id = dto.id,
            name = dto.name,
            url = dto.url,
            level = dto.level,
            complexity = dto.complexity,
            rarity = dto.rarity,
            source = dto.source,
            sourceType = dto.sourceType
        ),
        dto.traits.map { HazardTrait(it) }
    )

    fun toDomain(entity: HazardWithTraits) = Hazard(
        id = entity.hazard.id,
        name = entity.hazard.name,
        url = entity.hazard.url,
        level = entity.hazard.level,
        rarity = entity.hazard.rarity,
        complexity = entity.hazard.complexity,
        source = entity.hazard.source,
        sourceType = entity.hazard.sourceType,
        traits = entity.traits.map { it.name }
    )

}
