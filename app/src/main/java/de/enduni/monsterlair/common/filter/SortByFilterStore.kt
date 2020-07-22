package de.enduni.monsterlair.common.filter

import de.enduni.monsterlair.common.domain.SortBy

interface SortByFilterStore {

    fun setSortBy(sortBy: SortBy)

}