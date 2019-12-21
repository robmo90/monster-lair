package de.enduni.monsterlair.hazards.persistence

import androidx.sqlite.db.SimpleSQLiteQuery
import de.enduni.monsterlair.common.domain.Complexity
import de.enduni.monsterlair.common.persistence.HazardDao
import de.enduni.monsterlair.common.persistence.HazardEntity
import de.enduni.monsterlair.common.persistence.database.HazardEntityMapper
import de.enduni.monsterlair.hazards.domain.Hazard
import timber.log.Timber


class HazardRepository(
    private val hazardDao: HazardDao,
    private val hazardEntityMapper: HazardEntityMapper
) {

    suspend fun getHazard(id: Long) =
        hazardDao.getHazard(id).let { hazardEntityMapper.toDomain(it) }

    suspend fun getHazards(): List<Hazard> {
        return hazardDao.getAllHazards().toDomain()
    }

    suspend fun getFilteredHazards(
        filter: String?,
        lowerLevel: Int,
        higherLevel: Int,
        sortBy: String,
        complexities: List<Complexity>
    ): List<Hazard> {
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
        return hazardDao.getFilteredHazards(SimpleSQLiteQuery(query)).toDomain()
    }


    private fun List<HazardEntity>.toDomain(): List<Hazard> {
        return this.map { hazardEntityMapper.toDomain(it) }
    }

}