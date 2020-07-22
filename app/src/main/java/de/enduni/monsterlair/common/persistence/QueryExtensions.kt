package de.enduni.monsterlair.common.persistence

fun List<Any>.buildQuery(column: String): String {
    return if (isEmpty()) {
        ""
    } else {
        "AND $column IN ${joinToString(
            prefix = "(\"",
            postfix = "\")",
            separator = "\", \""
        )}"
    }
}