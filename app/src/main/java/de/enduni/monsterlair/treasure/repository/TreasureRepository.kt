package de.enduni.monsterlair.treasure.repository

import androidx.sqlite.db.SimpleSQLiteQuery
import de.enduni.monsterlair.common.domain.Rarity
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

    suspend fun getTreasures(filter: TreasureFilter): List<Treasure> {
        val query = buildQuery(
            filter.searchTerm,
            filter.categories,
            filter.lowerLevel,
            filter.upperLevel,
            filter.lowerGoldCost,
            filter.upperGoldCost,
            filter.rarities,
            getSortByString(filter.sortBy)
        )
        return dao.getTreasure(SimpleSQLiteQuery(query))
            .asSequence()
            .filter { treasureWithTraits ->
                if (filter.traits.isEmpty()) {
                    true
                } else {
                    filter.traits.any {
                        treasureWithTraits.traits.map { treasureTrait -> treasureTrait.name }
                            .contains(it)
                    }
                }
            }
            .filter { treasureWithTraits ->
                if (filter.searchTerm.isBlank()) {
                    true
                } else {
                    treasureWithTraits.traits.any {
                        it.name.contains(
                            filter.searchTerm,
                            ignoreCase = true
                        )
                    }
                }
            }
//            .filter { treasureWithTraits ->
//                when {
//                    filter.upperGoldCost == null && filter.lowerGoldCost != null -> filter.lowerGoldCost < treasureWithTraits.treasure.priceInGp
//                    filter.upperGoldCost != null && filter.lowerGoldCost == null -> filter.upperGoldCost > treasureWithTraits.treasure.priceInGp
//                    filter.upperGoldCost != null && filter.lowerGoldCost != null -> filter.lowerGoldCost < treasureWithTraits.treasure.priceInGp && filter.upperGoldCost > treasureWithTraits.treasure.priceInGp
//                    else -> true
//                }
//            }
            .map { mapper.fromEntityToDomain(it) }
            .filter { treasure -> filter.traits.all { treasure.traits.contains(it) } }
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
        categories: List<TreasureCategory>,
        lowerLevel: Int,
        higherLevel: Int,
        lowerGoldCost: Double?,
        upperGoldCost: Double?,
        rarities: List<Rarity>,
        sortBy: String
    ): String {
        val filterString = if (filter.isNullOrEmpty()) "\"%\"" else "\"%${filter}%\""
        val typeFilterString = if (categories.isEmpty()) {
            ""
        } else {
            "AND CATEGORY IN ${categories.joinToString(
                prefix = "(\"",
                postfix = "\")",
                separator = "\", \""
            )} "
        }
        val rarityFilterString = if (rarities.isEmpty()) {
            ""
        } else {
            "AND RARITY IN ${rarities.joinToString(
                prefix = "(\"",
                postfix = "\")",
                separator = "\", \""
            )} "
        }
        val goldFilterString = when {
            upperGoldCost == null && lowerGoldCost != null -> "AND priceInGp BETWEEN $lowerGoldCost AND ${90000.0} "
            upperGoldCost != null && lowerGoldCost == null -> "AND priceInGp BETWEEN ${0.0} AND $upperGoldCost "
            upperGoldCost != null && lowerGoldCost != null -> "AND priceInGp BETWEEN $lowerGoldCost AND $upperGoldCost "
            else -> ""
        }
        val query = "SELECT * FROM treasures WHERE " +
                "(name LIKE $filterString OR category LIKE $filterString) " +
                "AND level BETWEEN $lowerLevel AND $higherLevel " +
                goldFilterString +
                typeFilterString +
                rarityFilterString +
                "ORDER BY $sortBy ASC"
        Timber.d("Using $query")
        return query
    }

    suspend fun getTraits(): List<String> {
        return dao.getTraits().map { it.name }
    }

}