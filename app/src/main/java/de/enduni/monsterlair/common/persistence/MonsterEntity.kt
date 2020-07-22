package de.enduni.monsterlair.common.persistence

import androidx.room.*
import de.enduni.monsterlair.common.domain.*
import de.enduni.monsterlair.common.persistence.database.EnumTypeConverters

@Entity(tableName = "monsters")
@TypeConverters(EnumTypeConverters::class)
data class MonsterEntity(
    @PrimaryKey val id: String,
    val name: String,
    val url: String,
    val family: String,
    val level: Int,
    val alignment: Alignment,
    val type: MonsterType,
    val rarity: Rarity,
    val size: Size,
    val source: String,
    val sourceType: Source
)

@Entity(tableName = "monsterTraits")
data class MonsterTrait(
    @PrimaryKey val name: String
)

@Entity(
    tableName = "MonsterAndTraitsCrossRef", primaryKeys = ["id", "name"],
    indices = [Index(
        value = ["name"],
        name = "monster_cross_ref_index"
    )]
)
data class MonsterAndTraitsCrossRef(
    val id: String,
    val name: String
)

data class MonsterWithTraits(
    @Embedded val monster: MonsterEntity,
    @Relation(
        parentColumn = "id",
        entityColumn = "name",
        associateBy = Junction(MonsterAndTraitsCrossRef::class)
    )
    val traits: List<MonsterTrait>
)