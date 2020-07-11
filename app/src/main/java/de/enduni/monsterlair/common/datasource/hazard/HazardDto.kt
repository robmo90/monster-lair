package de.enduni.monsterlair.common.datasource.hazard

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass
import de.enduni.monsterlair.common.domain.Complexity
import de.enduni.monsterlair.common.domain.Rarity
import de.enduni.monsterlair.common.domain.Source

@JsonClass(generateAdapter = true)
data class HazardDto(
    @Json(name = "id") var id: String,
    @Json(name = "name") val name: String,
    @Json(name = "aonUrl") val url: String,
    @Json(name = "level") val level: Int,
    @Json(name = "complexity") val complexity: Complexity,
    @Json(name = "rarity") val rarity: Rarity,
    @Json(name = "source") val source: String,
    @Json(name = "sourceType") val sourceType: Source,
    @Json(name = "traits") val traits: List<String>
)