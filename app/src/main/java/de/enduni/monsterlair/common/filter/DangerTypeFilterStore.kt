package de.enduni.monsterlair.common.filter

import de.enduni.monsterlair.creator.view.DangerType

interface DangerTypeFilterStore {

    fun addDangerType(dangerType: DangerType)
    fun removeDangerType(dangerType: DangerType)

}