package de.enduni.monsterlair.treasure.repository

import androidx.sqlite.db.SimpleSQLiteQuery
import de.enduni.monsterlair.common.domain.TreasureCategory
import de.enduni.monsterlair.common.persistence.TreasureDao
import de.enduni.monsterlair.monsters.view.SortBy
import de.enduni.monsterlair.treasure.domain.Treasure
import de.enduni.monsterlair.treasure.domain.TreasureFilter
import timber.log.Timber

class TreasureRepository(
    private val dao: TreasureDao,
    private val mapper: TreasureEntityMapper
) {

    suspend fun getTreasures(treasureFilter: TreasureFilter): List<Treasure> {
        val query = buildQuery(
            treasureFilter.searchString,
            treasureFilter.categories,
            treasureFilter.lowerLevel,
            treasureFilter.upperLevel,
            getSortByString(treasureFilter.sortBy)
        )
        return dao.getTreasure(SimpleSQLiteQuery(query))
            .asSequence()
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

    private fun getSortByString(sortBy: SortBy): String {
        return when (sortBy) {
            SortBy.TYPE -> "category"
            else -> sortBy.value
        }
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
            "AND CATEGORY IN ${monsterTypes.joinToString(
                prefix = "(\"",
                postfix = "\")",
                separator = "\", \""
            )}"
        }
        val query = "SELECT * FROM treasures WHERE " +
                "(name LIKE $filterString OR category LIKE $filterString) " +
                "AND level BETWEEN $lowerLevel AND $higherLevel " +
                typeFilterString +
                "ORDER BY $sortBy ASC"
        Timber.v("Using $query")
        return query
    }

}