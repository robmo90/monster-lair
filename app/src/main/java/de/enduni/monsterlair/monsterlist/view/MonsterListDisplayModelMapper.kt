package de.enduni.monsterlair.monsterlist.view

import de.enduni.monsterlair.monsterlist.domain.Monster

class MonsterListDisplayModelMapper {

    fun fromDomain(monster: Monster) = MonsterListDisplayModel(
        name = monster.name,
        url = monster.url,
        level = monster.level,
        type = monster.type,
        family = monster.family
    )
}