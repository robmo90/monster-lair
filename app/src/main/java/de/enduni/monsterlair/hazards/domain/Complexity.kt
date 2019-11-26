package de.enduni.monsterlair.hazards.domain

import com.squareup.moshi.FromJson

enum class Complexity {
    SIMPLE,
    COMPLEX
}

class ComplexityTypeAdapter {

    @FromJson
    fun fromJson(string: String): Complexity {
        val type = Complexity.values().find { it.toString() == string }
        return type ?: Complexity.SIMPLE
    }
}