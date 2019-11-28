package de.enduni.monsterlair.hazards.persistence

import androidx.sqlite.db.SimpleSQLiteQuery
import de.enduni.monsterlair.common.persistence.HazardDao
import de.enduni.monsterlair.common.persistence.HazardEntity
import de.enduni.monsterlair.common.persistence.database.HazardEntityMapper
import de.enduni.monsterlair.hazards.domain.Hazard
import de.enduni.monsterlair.hazards.view.HazardType
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
        type: HazardType
    ): List<Hazard> {
        val filterString = if (filter.isNullOrEmpty()) "\"%\"" else "\"%${filter}%\""
        val typeFilterString = when (type) {
            HazardType.ALL -> ""
            HazardType.SIMPLE -> "AND complexity is \"SIMPLE\""
            HazardType.COMPLEX -> "AND complexity is \"COMPLEX\""
        }
        val query =
            "SELECT * FROM hazards WHERE name LIKE $filterString AND level BETWEEN $lowerLevel AND $higherLevel $typeFilterString ORDER BY name ASC"
        Timber.v("Using $query")
        return hazardDao.getFilteredHazards(SimpleSQLiteQuery(query)).toDomain()
    }


    private fun List<HazardEntity>.toDomain(): List<Hazard> {
        return this.map { hazardEntityMapper.toDomain(it) }
    }

}