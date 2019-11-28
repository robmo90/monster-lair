package de.enduni.monsterlair.hazards.persistence

import de.enduni.monsterlair.common.persistence.HazardDao
import de.enduni.monsterlair.common.persistence.HazardEntity
import de.enduni.monsterlair.common.persistence.database.HazardEntityMapper
import de.enduni.monsterlair.hazards.domain.Hazard


class HazardRepository(
    private val hazardDao: HazardDao,
    private val hazardEntityMapper: HazardEntityMapper
) {

    suspend fun getHazard(id: Long) =
        hazardDao.getHazard(id).let { hazardEntityMapper.toDomain(it) }

    suspend fun getHazards(): List<Hazard> {
        return hazardDao.getAllHazards().toDomain()
    }


    private fun List<HazardEntity>.toDomain(): List<Hazard> {
        return this.map { hazardEntityMapper.toDomain(it) }
    }

}