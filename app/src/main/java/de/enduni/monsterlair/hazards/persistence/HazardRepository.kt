package de.enduni.monsterlair.hazards.persistence

import androidx.sqlite.db.SimpleSQLiteQuery
import de.enduni.monsterlair.common.domain.Complexity
import de.enduni.monsterlair.common.domain.Level
import de.enduni.monsterlair.common.domain.Rarity
import de.enduni.monsterlair.common.domain.Trait
import de.enduni.monsterlair.common.persistence.HazardDao
import de.enduni.monsterlair.common.persistence.HazardWithTraits
import de.enduni.monsterlair.common.persistence.buildQuery
import de.enduni.monsterlair.common.persistence.database.HazardEntityMapper
import de.enduni.monsterlair.common.sources.SourceManager
import de.enduni.monsterlair.hazards.domain.Hazard
import timber.log.Timber


class HazardRepository(
    private val hazardDao: HazardDao,
    private val hazardEntityMapper: HazardEntityMapper,
    private val sourceManager: SourceManager
) {

    suspend fun getHazard(id: String) =
        hazardDao.getHazard(id).let { hazardEntityMapper.toDomain(it) }

    suspend fun getFilteredHazards(
        searchTerm: String,
        complexities: List<Complexity>,
        rarities: List<Rarity>,
        lowerLevel: Level,
        upperLevel: Level,
        sortByString: String,
        traits: List<Trait>
    ): List<Hazard> {
        val query = buildQuery(
            searchTerm,
            complexities,
            rarities,
            lowerLevel,
            upperLevel,
            sortByString
        )
        return hazardDao.getFilteredHazards(SimpleSQLiteQuery(query))
            .toDomain()
            .filter { hazard ->
                if (traits.isEmpty()) {
                    true
                } else {
                    traits.any { hazard.traits.contains(it) }
                }
            }
    }

    private fun buildQuery(
        filter: String,
        complexities: List<Complexity>,
        rarities: List<Rarity>,
        lowerLevel: Int,
        higherLevel: Int,
        sortBy: String
    ): String {
        val filterString = if (filter.isBlank()) "\"%\"" else "\"%${filter}%\""
        val typeFilterString = complexities.buildQuery("complexity")
        val rarityFilterString = rarities.buildQuery("rarity")
        val sourceFilterString = sourceManager.sources.buildQuery("sourceType")
        val query =
            "SELECT * FROM hazards WHERE name LIKE $filterString AND level BETWEEN $lowerLevel AND $higherLevel $typeFilterString $rarityFilterString $sourceFilterString ORDER BY $sortBy ASC"
        Timber.v("Using $query")
        return query
    }


    private fun List<HazardWithTraits>.toDomain(): List<Hazard> {
        return this.map { hazardEntityMapper.toDomain(it) }
    }

    suspend fun getTraits(): List<Trait> = hazardDao.getTraits().map { it.name }

}