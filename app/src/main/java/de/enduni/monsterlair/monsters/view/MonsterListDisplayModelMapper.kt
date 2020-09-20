package de.enduni.monsterlair.monsters.view

import de.enduni.monsterlair.common.domain.Source
import de.enduni.monsterlair.monsters.domain.Monster

class MonsterListDisplayModelMapper {

    fun toMonsterDisplayModel(monster: Monster) = MonsterListDisplayModel(
        id = monster.id,
        name = monster.name,
        level = monster.level,
        type = monster.type,
        family = monster.family,
        custom = monster.sourceType == Source.CUSTOM,
        alignment = monster.alignment,
        size = monster.size,
        rarity = monster.rarity,
        traits = monster.traits,
        description = monster.description
    )

}