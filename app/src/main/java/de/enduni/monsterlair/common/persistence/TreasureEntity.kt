package de.enduni.monsterlair.common.persistence

import androidx.room.*
import de.enduni.monsterlair.common.domain.Rarity
import de.enduni.monsterlair.common.domain.Source
import de.enduni.monsterlair.common.domain.TreasureCategory
import de.enduni.monsterlair.common.persistence.database.EnumTypeConverters

@Entity(tableName = "treasures")
@TypeConverters(EnumTypeConverters::class)
data class TreasureEntity(
    @PrimaryKey val id: String,
    val name: String,
    val url: String,
    val level: Int,
    val category: TreasureCategory,
    val price: String,
    val priceInGp: Double,
    val source: String,
    val sourceType: Source,
    val rarity: Rarity
)

@Entity(tableName = "treasureTraits")
data class TreasureTrait(
    @PrimaryKey val name: String
)

@Entity(tableName = "TreasureAndTraitsCrossRef", primaryKeys = ["id", "name"])
data class TreasureAndTraitsCrossRef(
    val id: String,
    val name: String
)

data class TreasureWithTraits(
    @Embedded val treasure: TreasureEntity,
    @Relation(
        parentColumn = "id",
        entityColumn = "name",
        associateBy = Junction(TreasureAndTraitsCrossRef::class)
    )
    val traits: List<TreasureTrait>
)