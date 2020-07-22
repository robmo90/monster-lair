package de.enduni.monsterlair.common.filter

interface GoldCostFilterStore {

    fun setUpperGoldCost(cost: Double?)
    fun setLowerGoldCost(cost: Double?)

}