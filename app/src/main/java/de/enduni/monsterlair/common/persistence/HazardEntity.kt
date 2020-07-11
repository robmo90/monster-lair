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

@Entity(tableName = "HazardsAndTraitsCrossRef", primaryKeys = ["hazardId", "traitName"])
data class HazardsAndTraitsCrossRef(
    val hazardId: Long,
    val traitName: String
)

data class HazardWithTraits(
    @Embedded val hazard: HazardEntity,
    @Relation(
        parentColumn = "hazardId",
        entityColumn = "traitName",
        associateBy = Junction(HazardsAndTraitsCrossRef::class)
    )
    val traits: List<MonsterTrait>
)