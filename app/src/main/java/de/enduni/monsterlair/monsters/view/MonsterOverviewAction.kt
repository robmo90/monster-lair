package de.enduni.monsterlair.monsters.view

import de.enduni.monsterlair.monsters.domain.Monster

sealed class MonsterOverviewAction {

    class OnMonsterLinkClicked(val url: String) : MonsterOverviewAction()
    object OnCustomMonsterClicked : MonsterOverviewAction()
    class OnCustomMonsterPressed(val id: Long, val monsterName: String) : MonsterOverviewAction()
    class OnEditCustomMonsterClicked(val monster: Monster) : MonsterOverviewAction()

}
