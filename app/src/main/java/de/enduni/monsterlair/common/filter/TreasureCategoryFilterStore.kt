package de.enduni.monsterlair.common.filter

import de.enduni.monsterlair.common.domain.TreasureCategory

interface TreasureCategoryFilterStore {

    fun addCategory(category: TreasureCategory)
    fun removeCategory(category: TreasureCategory)

}