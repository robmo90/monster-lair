package de.enduni.monsterlair.hazards.view

import de.enduni.monsterlair.hazards.domain.Hazard

class HazardDisplayModelMapper {

    fun toDisplayModel(domain: Hazard) = HazardDisplayModel(
        id = domain.id,
        name = domain.name,
        url = domain.url,
        level = domain.level,
        complexity = domain.complexity,
        source = domain.source
    )

}
