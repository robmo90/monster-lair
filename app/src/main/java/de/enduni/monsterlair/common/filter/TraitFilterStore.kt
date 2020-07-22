package de.enduni.monsterlair.common.filter

import de.enduni.monsterlair.common.domain.Trait

interface TraitFilterStore {

    fun addTrait(trait: Trait)
    fun removeTrait(trait: Trait)

}