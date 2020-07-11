package de.enduni.monsterlair.hazards.persistence

import androidx.sqlite.db.SimpleSQLiteQuery
import de.enduni.monsterlair.common.domain.Complexity
import de.enduni.monsterlair.common.persistence.HazardDao
import de.enduni.monsterlair.common.persistence.HazardEntity
import de.enduni.monsterlair.common.persistence.database.HazardEntityMapper
import de.enduni.monsterlair.hazards.domain.Hazard
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import timber.log.Timber


class HazardRepository(
    private val hazardDao: HazardDao,
    private val hazardEntityMapper: HazardEntityMapper
) {

    suspend fun getHazard(id: String) =
        hazardDao.getHazard(id).let { hazardEntityMapper.toDomain(it) }

    suspend fun getFilteredHazards(
        filter: String?,
        lowerLevel: Int,
        higherLevel: Int,
        sortBy: String,
        complexities: List<Complexity>
    ): List<Hazard> {
        val query = buildQuery(filter, complexities, lowerLevel, higherLevel, sortBy)
        return hazardDao.getFilteredHazards(SimpleSQLiteQuery(query)).toDomain()
    }

    fun getFilteredHazardFlow(
        filter: String?,
        lowerLevel: Int,
        higherLevel: Int,
        sortBy: String,
        complexities: List<Complexity>
    ): Flow<List<Hazard>> {
        val query = buildQuery(filter, complexities, lowerLevel, higherLevel, sortBy)
        return hazardDao.getFilteredHazardFlow(SimpleSQLiteQuery(query)).map { it.toDomain() }
    }

    private fun buildQuery(
        filter: String?,
        complexities: List<Complexity>,
        lowerLevel: Int,
        higherLevel: Int,
        sortBy: String
    ): String {
        val filterString = if (filter.isNullOrEmpty()) "\"%\"" else "\"%${filter}%\""
        val typeFilterString = if (complexities.isEmpty()) {
            ""
        } else {
            "AND complexity IN ${complexities.joinToString(
                prefix = "(\"",
                postfix = "\")",
                separator = "\", \""
            )}"
        }
        val query =
            "SELECT * FROM hazards WHERE name LIKE $filterString AND level BETWEEN $lowerLevel AND $higherLevel $typeFilterString ORDER BY $sortBy ASC"
        Timber.v("Using $query")
        return query
    }


    private fun List<HazardEntity>.toDomain(): List<Hazard> {
        return this.map { hazardEntityMapper.toDomain(it) }
    }

}