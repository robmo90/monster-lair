package de.enduni.monsterlair.monsters.view

import androidx.lifecycle.ViewModel
import androidx.lifecycle.liveData
import de.enduni.monsterlair.monsters.persistence.MonsterRepository
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.collect

@ExperimentalCoroutinesApi
class MonsterFilterViewModel(
    val filterStore: MonsterFilterStore,
    private val treasureRepository: MonsterRepository
) : ViewModel() {

    val filter = liveData {
        filterStore.filter.collect { filter -> emit(filter) }
    }

    val traits = liveData {
        emit(treasureRepository.getTraits())
    }

}