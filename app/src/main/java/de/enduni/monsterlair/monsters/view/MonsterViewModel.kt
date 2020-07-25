package de.enduni.monsterlair.monsters.view

import androidx.lifecycle.*
import de.enduni.monsterlair.common.view.ActionLiveData
import de.enduni.monsterlair.common.view.CreateMonsterDialog
import de.enduni.monsterlair.common.view.EditMonsterDialog
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
) : ViewModel(), MonsterViewHolder.MonsterViewHolderListener,
    CreateMonsterDialog.OnSaveClickedListener,
    EditMonsterDialog.OnEditMonsterClickListener {

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


    override fun onSelect(monsterId: String) {
        viewModelScope.launch(handler) {
            val url = retrieveMonsterUseCase.execute(monsterId).url
            if (url.isBlank()) {
                _actions.sendAction(MonsterOverviewAction.OnCustomMonsterClicked)
            } else {
                _actions.sendAction(MonsterOverviewAction.OnMonsterLinkClicked(url))
            }
        }
    }


    private fun List<Monster>.toDisplayModel(): List<MonsterListDisplayModel> {
        return this.map { mapper.toMonsterDisplayModel(it) }
    }

    override fun onSaveClicked(monster: Monster) {
        viewModelScope.launch(handler + Dispatchers.Default) {
            saveMonsterUseCase.execute(monster)
            filterStore.refresh()
        }
    }

    override fun onEditClicked(id: String) {
        viewModelScope.launch(handler) {
            val monster = retrieveMonsterUseCase.execute(id)
            _actions.sendAction(MonsterOverviewAction.OnEditCustomMonsterClicked(monster))
        }
    }

    override fun onDeleteClicked(id: String) {
        viewModelScope.launch(handler + Dispatchers.Default) {
            deleteMonsterUseCase.execute(id)
            filterStore.refresh()
        }
    }

    override fun onLongPress(monsterId: String) {
        viewModelScope.launch(handler) {
            val monster = retrieveMonsterUseCase.execute(monsterId)
            _actions.sendAction(
                MonsterOverviewAction.OnCustomMonsterPressed(
                    monsterId,
                    monster.name
                )
            )
        }
    }
}
