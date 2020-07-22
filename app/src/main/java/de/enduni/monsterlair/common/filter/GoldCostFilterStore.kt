package de.enduni.monsterlair.common.filter

import de.enduni.monsterlair.common.domain.Cost

interface GoldCostFilterStore {

    fun setUpperGoldCost(cost: Cost)
    fun setLowerGoldCost(cost: Cost)

}