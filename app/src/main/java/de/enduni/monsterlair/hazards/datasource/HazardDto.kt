package de.enduni.monsterlair.hazards.datasource

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass
import de.enduni.monsterlair.hazards.domain.Complexity

@JsonClass(generateAdapter = true)
data class HazardDto(
    @Json(name = "id") val id: Int,
    @Json(name = "name") val name: String,
    @Json(name = "aonUrl") val url: String,
    @Json(name = "level") val level: Int,
    @Json(name = "complexity") val complexity: Complexity,
    @Json(name = "source") val source: String
)