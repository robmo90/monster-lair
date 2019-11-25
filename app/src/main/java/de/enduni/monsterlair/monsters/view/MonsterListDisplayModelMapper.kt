package de.enduni.monsterlair.monsters.view

import de.enduni.monsterlair.monsters.domain.Monster

class MonsterListDisplayModelMapper {

    fun toMonsterDisplayModel(monster: Monster) = MonsterListDisplayModel(
        name = monster.name,
        url = monster.url,
        level = monster.level,
        type = monster.type,
        family = monster.family
    )

}