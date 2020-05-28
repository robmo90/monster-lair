package de.enduni.monsterlair.common.datasource.hazard

interface HazardDataSource {

    suspend fun getHazards(): List<HazardDto>

    suspend fun getHazardUpdate(version: Long): List<HazardDto>
}