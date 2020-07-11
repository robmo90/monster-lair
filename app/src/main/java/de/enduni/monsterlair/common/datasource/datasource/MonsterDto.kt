package de.enduni.monsterlair.common.datasource.datasource

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass
import de.enduni.monsterlair.common.domain.*

@JsonClass(generateAdapter = true)
data class MonsterDto(
    @Json(name = "id") val id: String,
    @Json(name = "name") val name: String,
    @Json(name = "aonUrl") val url: String,
    @Json(name = "family") val family: String,
    @Json(name = "level") val level: Int,
    @Json(name = "alignment") val alignment: Alignment,
    @Json(name = "type") val type: MonsterType,
    @Json(name = "size") val size: Size,
    @Json(name = "source") val source: String,
    @Json(name = "sourceType") val sourceType: Source,
    @Json(name = "rarity") val rarity: Rarity,
    @Json(name = "traits") val traits: List<String>
)