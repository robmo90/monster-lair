package de.enduni.monsterlair.common.filter

import de.enduni.monsterlair.common.domain.Level

interface LevelFilterStore {

    fun setUpperLevel(level: Level)
    fun setLowerLevel(level: Level)

}