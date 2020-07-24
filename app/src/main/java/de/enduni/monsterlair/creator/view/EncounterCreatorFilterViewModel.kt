package de.enduni.monsterlair.creator.view

import androidx.lifecycle.ViewModel
import androidx.lifecycle.liveData
import de.enduni.monsterlair.hazards.persistence.HazardRepository
import de.enduni.monsterlair.monsters.persistence.MonsterRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.collect

class EncounterCreatorFilterViewModel(
    val filterStore: EncounterCreatorFilterStore,
    private val monsterRepository: MonsterRepository,
    private val hazardRepository: HazardRepository
) : ViewModel() {

    val filter = liveData {
        filterStore.filter.collect { filter -> emit(filter) }
    }

    val traits = liveData(Dispatchers.IO) {
        emit(
            (monsterRepository.getTraits() +
                    hazardRepository.getTraits())
                .sorted()
        )
    }


}