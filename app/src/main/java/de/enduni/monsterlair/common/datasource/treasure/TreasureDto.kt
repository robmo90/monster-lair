package de.enduni.monsterlair.common.datasource.treasure

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass
import de.enduni.monsterlair.common.domain.Rarity
import de.enduni.monsterlair.common.domain.Source
import de.enduni.monsterlair.common.domain.TreasureCategory

@JsonClass(generateAdapter = true)
data class TreasureDto(
    @Json(name = "id") val id: String,
    @Json(name = "name") val name: String,
    @Json(name = "aonUrl") val url: String,
    @Json(name = "level") val level: Int,
    @Json(name = "category") val category: TreasureCategory,
    @Json(name = "price") val price: String,
    @Json(name = "source") val source: String,
    @Json(name = "sourceType") val sourceType: Source,
    @Json(name = "rarity") val rarity: Rarity,
    @Json(name = "traits") val traits: List<String>
)