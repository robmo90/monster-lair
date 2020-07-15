package de.enduni.monsterlair.treasure.view

import androidx.annotation.DrawableRes

data class TreasureDisplayModel(
    val id: String,
    val name: String,
    val url: String,
    val caption: String,
    @DrawableRes val icon: Int
)