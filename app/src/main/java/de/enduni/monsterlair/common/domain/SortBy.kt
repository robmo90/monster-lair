package de.enduni.monsterlair.common.domain

enum class SortBy(val value: String) {
    NAME("name"),
    LEVEL("level"),
    TYPE("type");

    fun getStringForHazard(): String {
        return when (this) {
            TYPE -> "complexity"
            else -> value
        }
    }

    fun getStringForTreasure(): String {
        return when (this) {
            TYPE -> "category"
            else -> value
        }
    }

}