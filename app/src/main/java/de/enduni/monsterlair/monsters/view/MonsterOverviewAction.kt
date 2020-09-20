package de.enduni.monsterlair.monsters.view

import de.enduni.monsterlair.monsters.domain.Monster

sealed class MonsterOverviewAction {

    class OnMonsterLinkClicked(val url: String) : MonsterOverviewAction()
    class OnEditCustomMonsterClicked(val monster: Monster) : MonsterOverviewAction()
    class OnDeleteCustomMonsterClicked(val monster: Monster) : MonsterOverviewAction()

}
