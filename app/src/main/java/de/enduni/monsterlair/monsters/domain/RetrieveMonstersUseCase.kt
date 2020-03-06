package de.enduni.monsterlair.monsters.domain

import de.enduni.monsterlair.monsters.persistence.MonsterRepository
import de.enduni.monsterlair.monsters.view.MonsterFilter
import kotlinx.coroutines.flow.Flow
import timber.log.Timber

class RetrieveMonstersUseCase(private val monsterRepository: MonsterRepository) {

    fun execute(filter: MonsterFilter): Flow<List<Monster>> {
        val currentTimeMillis = System.currentTimeMillis()
        val monsters =
            monsterRepository.getMonsterFlow(
                filter.string,
                filter.lowerLevel,
                filter.upperLevel,
                filter.sortBy.value,
                filter.monsterTypes
            )
        val afterTimeMillis = System.currentTimeMillis()
        Timber.v("Taking ${afterTimeMillis - currentTimeMillis} to filter")
        return monsters
    }

}

