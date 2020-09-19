package de.enduni.monsterlair.monsters.view

import de.enduni.monsterlair.common.domain.Alignment
import de.enduni.monsterlair.common.domain.MonsterType
import de.enduni.monsterlair.common.domain.Rarity
import de.enduni.monsterlair.common.domain.Size

data class MonsterListDisplayModel(
    val id: String,
    val name: String,
    val family: String,
    val level: Int,
    val type: MonsterType,
    val custom: Boolean,
    val alignment: Alignment,
    val rarity: Rarity,
    val size: Size,
    val traits: List<String>,
    val description: String
)