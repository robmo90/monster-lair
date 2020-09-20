package de.enduni.monsterlair.monsters.view

import androidx.lifecycle.*
import de.enduni.monsterlair.common.view.ActionLiveData
import de.enduni.monsterlair.monsters.domain.*
import de.enduni.monsterlair.monsters.view.adapter.MonsterViewHolder
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import timber.log.Timber

@ExperimentalCoroutinesApi
class MonsterViewModel(
    val filterStore: MonsterFilterStore,
    private val retrieveMonsterUseCase: RetrieveMonsterUseCase,
    private val retrieveMonstersUseCase: RetrieveMonstersUseCase,
    private val deleteMonsterUseCase: DeleteMonsterUseCase,
    private val saveMonsterUseCase: SaveMonsterUseCase,
    private val mapper: MonsterListDisplayModelMapper
) : ViewModel(), MonsterViewHolder.MonsterViewHolderListener {

    private val _actions = ActionLiveData<MonsterOverviewAction>()
    val actions: LiveData<MonsterOverviewAction> get() = _actions

    private val handler = CoroutineExceptionHandler { _, exception ->
        Timber.e(exception, "Caught exception")
    }

    val monsters = liveData {
        filterStore.filter.collect { monsterFilter ->
            retrieveMonstersUseCase.execute(monsterFilter)
                .toDisplayModel()
                .let { emit(it) }
        }

    }

    val filter = filterStore.filter.asLiveData()


    override fun onOpenArchive(monsterId: String) {
        viewModelScope.launch(handler) {
            val url = retrieveMonsterUseCase.execute(monsterId).url
            _actions.sendAction(MonsterOverviewAction.OnMonsterLinkClicked(url))
        }
    }


    private fun List<Monster>.toDisplayModel(): List<MonsterListDisplayModel> {
        return this.map { mapper.toMonsterDisplayModel(it) }
    }

    override fun onEditClicked(monsterId: String) {
        viewModelScope.launch(handler) {
            val monster = retrieveMonsterUseCase.execute(monsterId)
            _actions.sendAction(MonsterOverviewAction.OnEditCustomMonsterClicked(monster))
        }
    }

    override fun onDeleteClicked(monsterId: String) {
        viewModelScope.launch(handler) {
            val monster = retrieveMonsterUseCase.execute(monsterId)
            _actions.sendAction(
                MonsterOverviewAction.OnDeleteCustomMonsterClicked(monster)
            )
        }
    }

    fun onDeleteConfirmed(monsterId: String) {
        viewModelScope.launch(handler + Dispatchers.Default) {
            deleteMonsterUseCase.execute(monsterId)
            filterStore.refresh()
        }
    }

}