package de.enduni.monsterlair.common.datasource.hazard

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass
import de.enduni.monsterlair.common.domain.Complexity

@JsonClass(generateAdapter = true)
data class HazardDto(
    @Json(name = "id") val id: Int,
    @Json(name = "name") val name: String,
    @Json(name = "aonUrl") val url: String,
    @Json(name = "level") val level: Int,
    @Json(name = "complexity") val complexity: Complexity,
    @Json(name = "source") val source: String
)