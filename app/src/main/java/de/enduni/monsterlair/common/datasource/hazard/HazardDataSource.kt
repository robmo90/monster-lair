package de.enduni.monsterlair.common.datasource.hazard

interface HazardDataSource {

    suspend fun getHazards(): List<HazardDto>

}