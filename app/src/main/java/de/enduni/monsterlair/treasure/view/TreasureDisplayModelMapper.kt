package de.enduni.monsterlair.treasure.view

import de.enduni.monsterlair.common.getIcon
import de.enduni.monsterlair.treasure.domain.Treasure

class TreasureDisplayModelMapper {

    fun fromDomainToDisplayModel(dto: Treasure) = TreasureDisplayModel(
        id = dto.id,
        name = dto.name,
        url = dto.url,
        icon = dto.category.getIcon(),
        caption = "${dto.traits.joinToString()} - Level: ${dto.level}"
    )

}