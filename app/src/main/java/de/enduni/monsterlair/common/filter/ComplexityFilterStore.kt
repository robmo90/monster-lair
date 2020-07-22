package de.enduni.monsterlair.common.filter

import de.enduni.monsterlair.common.domain.Complexity

interface ComplexityFilterStore {

    fun addComplexity(complexity: Complexity)
    fun removeComplexity(complexity: Complexity)

}