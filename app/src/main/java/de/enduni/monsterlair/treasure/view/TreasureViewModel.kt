package de.enduni.monsterlair.treasure.view

import androidx.lifecycle.*
import de.enduni.monsterlair.common.domain.Level
import de.enduni.monsterlair.common.view.ActionLiveData
import de.enduni.monsterlair.common.view.CreateRandomTreasureDialog
import de.enduni.monsterlair.treasure.domain.CreateRandomTreasureTextUseCase
import de.enduni.monsterlair.treasure.domain.Treasure
import de.enduni.monsterlair.treasure.domain.TreasureFilter
import de.enduni.monsterlair.treasure.repository.TreasureRepository
import de.enduni.monsterlair.treasure.view.adapter.TreasureViewHolder
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import timber.log.Timber

@ExperimentalCoroutinesApi
class TreasureViewModel(
    val filterStore: TreasureFilterStore,
    private val treasureRepository: TreasureRepository,
    private val mapper: TreasureDisplayModelMapper,
    private val createRandomTreasureTextUseCase: CreateRandomTreasureTextUseCase
) : ViewModel(), TreasureViewHolder.TreasureViewHolderListener,
    CreateRandomTreasureDialog.Listener {

    private val handler = CoroutineExceptionHandler { _, exception ->
        Timber.e(exception, "Caught exception")
    }

    private val _actions = ActionLiveData<TreasureAction>()
    val actions: LiveData<TreasureAction> get() = _actions

    val traits = liveData {
        emit(treasureRepository.getTraits())
    }

    val filter = MutableLiveData<TreasureFilter>()

    val treasures = liveData(Dispatchers.IO + handler) {
        filterStore.filter
            .collect { treasureFilter ->
                treasureRepository.getTreasures(treasureFilter)
                    .toDisplayModel()
                    .let { emit(it) }
                filter.postValue(treasureFilter)
            }
    }

    private fun List<Treasure>.toDisplayModel(): List<TreasureDisplayModel> {
        return this.map { mapper.fromDomainToDisplayModel(it) }
    }

    override fun onSelect(monsterId: String) {
        Timber.d("Do nothing")
    }

    override fun onCreateClicked(level: Level, numberOfPlayers: Int) {
        val handler = CoroutineExceptionHandler { _, _ ->
            viewModelScope.launch { _actions.sendAction(TreasureAction.NotEnoughTreasure) }
        }
        viewModelScope.launch(handler) {
            val html = createRandomTreasureTextUseCase.execute(level, numberOfPlayers)
            _actions.sendAction(TreasureAction.GeneratedTreasure(html))
        }
    }
}