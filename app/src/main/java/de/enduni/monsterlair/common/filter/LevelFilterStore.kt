package de.enduni.monsterlair.common.filter

interface LevelFilterStore {

    fun setUpperLevel(level: Int)
    fun setLowerLevel(level: Int)

}