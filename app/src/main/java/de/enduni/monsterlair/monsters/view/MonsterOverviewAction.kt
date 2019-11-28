package de.enduni.monsterlair.monsters.view

sealed class MonsterOverviewAction {

    class MonsterSelected(val url: String) : MonsterOverviewAction()

}
