package de.enduni.monsterlair.common.filter

import de.enduni.monsterlair.common.domain.MonsterType

interface MonsterTypeFilterStore {

    fun addType(type: MonsterType)
    fun removeType(type: MonsterType)

}