package de.enduni.monsterlair.monsters.domain

import de.enduni.monsterlair.monsters.persistence.MonsterRepository
import de.enduni.monsterlair.monsters.view.MonsterFilter
import timber.log.Timber

class RetrieveMonstersUseCase(private val monsterRepository: MonsterRepository) {

    suspend fun execute(filter: MonsterFilter): List<Monster> {
        val currentTimeMillis = System.currentTimeMillis()
        val filterString = if (filter.string.isNullOrEmpty()) "\"%\"" else "\"%${filter.string}%\""
        val monsters =
            monsterRepository.getMonsters(
                filterString,
                filter.lowerLevel,
                filter.upperLevel,
                filter.sortBy.value
            )
        val afterTimeMillis = System.currentTimeMillis()
        Timber.v("Taking ${afterTimeMillis - currentTimeMillis} to filter")
        return monsters
    }

}

