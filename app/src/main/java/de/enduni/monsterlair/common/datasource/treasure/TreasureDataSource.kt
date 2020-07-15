package de.enduni.monsterlair.common.datasource.treasure

interface TreasureDataSource {


    suspend fun getTreasures(): List<TreasureDto>

}