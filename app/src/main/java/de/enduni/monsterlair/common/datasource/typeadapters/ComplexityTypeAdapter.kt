package de.enduni.monsterlair.common.datasource.typeadapters

import com.squareup.moshi.FromJson
import de.enduni.monsterlair.common.domain.Complexity

class ComplexityTypeAdapter {

    @FromJson
    fun fromJson(string: String): Complexity {
        val type = Complexity.values().find { it.toString() == string }
        return type ?: Complexity.SIMPLE
    }
}