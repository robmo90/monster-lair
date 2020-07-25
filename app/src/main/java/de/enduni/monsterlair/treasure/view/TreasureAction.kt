package de.enduni.monsterlair.treasure.view

sealed class TreasureAction {

    data class GeneratedTreasure(val html: String) : TreasureAction()
    object NotEnoughTreasure : TreasureAction()

}