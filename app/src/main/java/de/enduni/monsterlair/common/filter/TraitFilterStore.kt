package de.enduni.monsterlair.common.filter

interface TraitFilterStore {

    fun addTrait(trait: String)
    fun removeTrait(trait: String)

}