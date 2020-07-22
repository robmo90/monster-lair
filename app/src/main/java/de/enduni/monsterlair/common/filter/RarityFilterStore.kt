package de.enduni.monsterlair.common.filter

import de.enduni.monsterlair.common.domain.Rarity

interface RarityFilterStore {

    fun addRarity(rarity: Rarity)
    fun removeRarity(rarity: Rarity)

}