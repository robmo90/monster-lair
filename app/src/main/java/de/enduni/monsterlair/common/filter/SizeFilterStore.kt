package de.enduni.monsterlair.common.filter

import de.enduni.monsterlair.common.domain.Size

interface SizeFilterStore {

    fun addSize(size: Size)
    fun removeSize(size: Size)

}