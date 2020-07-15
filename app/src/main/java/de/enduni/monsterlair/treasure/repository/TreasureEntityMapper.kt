package de.enduni.monsterlair.treasure.repository

import de.enduni.monsterlair.common.datasource.treasure.TreasureDto
import de.enduni.monsterlair.common.persistence.TreasureEntity
import de.enduni.monsterlair.common.persistence.TreasureTrait
import de.enduni.monsterlair.common.persistence.TreasureWithTraits
import de.enduni.monsterlair.treasure.domain.Treasure

class TreasureEntityMapper {


    fun fromDtoToEntity(dto: TreasureDto) = Pair(
        TreasureEntity(
            id = dto.id,
            name = dto.name,
            url = dto.url,
            level = dto.level,
            category = dto.category,
            price = dto.price,
            priceInGp = determinePriceInGp(dto.price) ?: 0.0,
            source = dto.source,
            sourceType = dto.sourceType,
            rarity = dto.rarity
        ),
        dto.traits.map { TreasureTrait(it) }
    )

    fun fromEntityToDomain(entity: TreasureWithTraits) = Treasure(
        id = entity.treasure.id,
        name = entity.treasure.name,
        url = entity.treasure.url,
        level = entity.treasure.level,
        category = entity.treasure.category,
        price = entity.treasure.price,
        priceInGp = entity.treasure.priceInGp,
        source = entity.treasure.source,
        sourceType = entity.treasure.sourceType,
        rarity = entity.treasure.rarity,
        traits = entity.traits.map { it.name }
    )

    private fun determinePriceInGp(price: String): Double? {
        return when {
            price.contains("gp") -> price.getAmount()
            price.contains("sp") -> price.getAmount()?.let { it * 0.1 }
            price.contains("cp") -> price.getAmount()?.let { it * 0.01 }
            else -> 0.0
        }
    }

    private fun String.getAmount() = this.split(" ").first().replace(",", "").toDoubleOrNull()

}