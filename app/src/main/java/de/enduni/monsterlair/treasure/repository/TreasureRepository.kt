package de.enduni.monsterlair.treasure.repository

import de.enduni.monsterlair.common.domain.TreasureCategory
import de.enduni.monsterlair.common.persistence.TreasureDao
import de.enduni.monsterlair.treasure.domain.Treasure
import de.enduni.monsterlair.treasure.domain.TreasureFilter
import timber.log.Timber

class TreasureRepository(
    private val dao: TreasureDao,
    private val mapper: TreasureEntityMapper
) {

    suspend fun getTreasures(treasureFilter: TreasureFilter): List<Treasure> {
        return dao.getTreasure()
            .asSequence()
            .filter { treasureWithTraits ->
                IntRange(
                    treasureFilter.lowerLevel,
                    treasureFilter.upperLevel
                ).contains(treasureWithTraits.treasure.level)
            }
            .filter { treasureWithTraits ->
                if (treasureFilter.categories.isEmpty()) {
                    true
                } else {
                    treasureFilter.categories.contains(treasureWithTraits.treasure.category)
                }
            }
            .filter { treasureWithTraits ->
                if (treasureFilter.traits.isEmpty()) {
                    true
                } else {
                    treasureFilter.traits.any {
                        treasureWithTraits.traits.map { treasureTrait -> treasureTrait.name }
                            .contains(it)
                    }
                }
            }
            .filter { treasureWithTraits ->
                if (treasureFilter.searchString.isBlank()) {
                    true
                } else {
                    treasureWithTraits.treasure.name.contains(
                        treasureFilter.searchString,
                        ignoreCase = true
                    ) ||
                            treasureWithTraits.treasure.category.toString()
                                .contains(treasureFilter.searchString, ignoreCase = true) ||
                            treasureWithTraits.traits.any {
                                it.name.contains(
                                    treasureFilter.searchString,
                                    ignoreCase = true
                                )
                            }
                }
            }
            .map { mapper.fromEntityToDomain(it) }
            .toList()
    }


    private fun buildQuery(
        filter: String?,
        monsterTypes: List<TreasureCategory>,
        lowerLevel: Int,
        higherLevel: Int,
        sortBy: String
    ): String {
        val filterString = if (filter.isNullOrEmpty()) "\"%\"" else "\"%${filter}%\""
        val typeFilterString = if (monsterTypes.isEmpty()) {
            ""
        } else {
            "AND TYPE IN ${monsterTypes.joinToString(
                prefix = "(\"",
                postfix = "\")",
                separator = "\", \""
            )}"
        }
        val query = "SELECT * FROM monsters WHERE " +
                "(name LIKE $filterString OR family LIKE $filterString) " +
                "AND level BETWEEN $lowerLevel AND $higherLevel " +
                typeFilterString +
                "ORDER BY $sortBy ASC"
        Timber.v("Using $query")
        return query
    }

}