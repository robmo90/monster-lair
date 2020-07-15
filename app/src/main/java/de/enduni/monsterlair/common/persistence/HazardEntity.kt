package de.enduni.monsterlair.common.persistence

import androidx.room.*
import de.enduni.monsterlair.common.domain.Complexity
import de.enduni.monsterlair.common.domain.Rarity
import de.enduni.monsterlair.common.domain.Source
import de.enduni.monsterlair.common.persistence.database.EnumTypeConverters

@Entity(tableName = "hazards")
@TypeConverters(EnumTypeConverters::class)
data class HazardEntity(
    @PrimaryKey val id: String,
    val name: String,
    val url: String,
    val level: Int,
    val complexity: Complexity,
    val rarity: Rarity,
    val source: String,
    val sourceType: Source
)

@Entity(tableName = "hazardTraits")
data class HazardTrait(
    @PrimaryKey val name: String
)

@Entity(tableName = "HazardsAndTraitsCrossRef", primaryKeys = ["id", "name"])
data class HazardsAndTraitsCrossRef(
    val id: String,
    val name: String
)

data class HazardWithTraits(
    @Embedded val hazard: HazardEntity,
    @Relation(
        parentColumn = "id",
        entityColumn = "name",
        associateBy = Junction(HazardsAndTraitsCrossRef::class)
    )
    val traits: List<MonsterTrait>
)