package de.enduni.monsterlair.monsterlist.datasource

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass
import de.enduni.monsterlair.monsterlist.domain.MonsterType

@JsonClass(generateAdapter = true)
data class MonsterDto(
    @Json(name = "name") val name: String,
    @Json(name = "aonUrl") val url: String,
    @Json(name = "family") val family: String,
    @Json(name = "level") val level: Int,
    @Json(name = "alignment") val alignment: String,
    @Json(name = "type") val type: MonsterType,
    @Json(name = "size") val size: String
)