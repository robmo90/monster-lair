package de.enduni.monsterlair.monsterlist.domain

import de.enduni.monsterlair.monsterlist.persistence.MonsterRepository
import de.enduni.monsterlair.monsterlist.view.MonsterFilter
import kotlinx.coroutines.flow.Flow
import timber.log.Timber

class RetrieveMonstersUseCase(private val monsterRepository: MonsterRepository) {

    suspend fun execute(filter: MonsterFilter): Flow<List<Monster>> {
        val currentTimeMillis = System.currentTimeMillis()
        val filterString = if (filter.string.isNullOrEmpty()) "%" else "%${filter.string}%"
        val monsters =
            monsterRepository.getMonsters(filterString, filter.lowerLevel, filter.upperLevel)
        val afterTimeMillis = System.currentTimeMillis()
        Timber.d("Taking ${afterTimeMillis - currentTimeMillis} to filter")
        return monsters
    }

}

