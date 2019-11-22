package de.enduni.monsterlair.overview

import android.annotation.SuppressLint
import kotlinx.coroutines.flow.map

class FilterMonstersUseCase(private val monsterRepository: MonsterRepository) {

    @SuppressLint("DefaultLocale")
    suspend fun execute(filter: MonsterFilter) = monsterRepository.getMonsters()
        .map { monsters ->
            var filteredMonsters = filter.string?.let { filterString ->
                monsters.filter {
                    val nameApplies = it.name.toLowerCase().contains(filterString.toLowerCase())
                    val familyApplies = it.family.toLowerCase().contains(filterString.toLowerCase())
                    nameApplies.or(familyApplies)
                }
            } ?: monsters
            filteredMonsters = filteredMonsters.filter { monster ->
                monster.level >= filter.lowerLevel && monster.level <= filter.higherLevel
            }

            filteredMonsters
        }

}

data class MonsterFilter(
    val string: String? = null,
    val lowerLevel: Int = -1,
    val higherLevel: Int = 25
)