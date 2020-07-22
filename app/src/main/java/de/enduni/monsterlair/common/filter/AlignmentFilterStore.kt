package de.enduni.monsterlair.common.filter

import de.enduni.monsterlair.common.domain.Alignment

interface AlignmentFilterStore {

    fun addAlignment(alignment: Alignment)
    fun removeAlignment(alignment: Alignment)

}