package de.enduni.monsterlair.hazards.datasource

interface HazardDataSource {

    suspend fun getHazards(): List<HazardDto>

}