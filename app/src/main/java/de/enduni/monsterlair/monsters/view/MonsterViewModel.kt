package de.enduni.monsterlair.monsters.view

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import de.enduni.monsterlair.common.domain.MonsterType
import de.enduni.monsterlair.common.view.ActionLiveData
import de.enduni.monsterlair.common.view.CreateMonsterDialog
import de.enduni.monsterlair.common.view.EditMonsterDialog
import de.enduni.monsterlair.monsters.domain.*
import de.enduni.monsterlair.monsters.view.adapter.MonsterViewHolder
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import timber.log.Timber

class MonsterViewModel(
    private val retrieveMonsterUseCase: RetrieveMonsterUseCase,
    private val retrieveMonstersUseCase: RetrieveMonstersUseCase,
    private val deleteMonsterUseCase: DeleteMonsterUseCase,
    private val saveMonsterUseCase: SaveMonsterUseCase,
    private val mapper: MonsterListDisplayModelMapper
) : ViewModel(), MonsterViewHolder.MonsterViewHolderListener,
    CreateMonsterDialog.OnSaveClickedListener,
    EditMonsterDialog.OnEditMonsterClickListener {

    private val _viewState = MutableLiveData<MonsterOverviewViewState>()
    val viewState: LiveData<MonsterOverviewViewState> get() = _viewState

    private val _actions = ActionLiveData<MonsterOverviewAction>()
    val actions: LiveData<MonsterOverviewAction> get() = _actions

    private val handler = CoroutineExceptionHandler { _, exception ->
        Timber.e(exception, "Caught exception")
    }

    private var filter = MonsterFilter()

    fun start() {
        viewModelScope.launch(Dispatchers.Default) {
            filterMonsters()
        }
    }


    fun filterByString(string: String) = viewModelScope.launch(handler) {
        filterMonsters(filter.copy(string = string))
    }

    fun adjustFilterLevelLower(lowerLevel: Int) = viewModelScope.launch(handler) {
        filterMonsters(filter.copy(lowerLevel = lowerLevel))
    }

    fun adjustFilterLevelUpper(upperLevel: Int) = viewModelScope.launch(handler) {
        filterMonsters(filter.copy(upperLevel = upperLevel))
    }

    fun adjustSortBy(sortBy: SortBy) = viewModelScope.launch(handler) {
        filterMonsters(filter.copy(sortBy = sortBy))
    }


    private suspend fun filterMonsters(newFilter: MonsterFilter) {
        if (newFilter != filter) {
            filter = newFilter
            filterMonsters()
        }
    }

    private suspend fun filterMonsters() {
        Timber.d("Starting monster filter with $filter")
        val monsters = retrieveMonstersUseCase.execute(filter).toDisplayModel()
        val state = MonsterOverviewViewState(
            monsters = monsters,
            filter = filter
        )
        _viewState.postValue(state)
    }


    override fun onSelect(monsterId: Long) {
        viewModelScope.launch(handler) {
            val monster = retrieveMonsterUseCase.execute(monsterId)
            monster.url?.let { _actions.sendAction(MonsterOverviewAction.OnMonsterLinkClicked(it)) }
                ?: _actions.sendAction(MonsterOverviewAction.OnCustomMonsterClicked)

        }
    }


    private fun List<Monster>.toDisplayModel(): List<MonsterListDisplayModel> {
        return this.map { mapper.toMonsterDisplayModel(it) }
    }

    fun addMonsterTypeFilter(monsterType: MonsterType) = viewModelScope.launch(handler) {
        filterMonsters(filter.copy(monsterTypes = filter.monsterTypes + monsterType))
    }

    fun removeMonsterTypeFilter(monsterType: MonsterType) = viewModelScope.launch(handler) {
        filterMonsters(filter.copy(monsterTypes = filter.monsterTypes - monsterType))
    }

    override fun onSaveClicked(monster: Monster) {
        viewModelScope.launch(handler + Dispatchers.Default) {
            saveMonsterUseCase.execute(monster)
            filterMonsters()
        }
    }

    override fun onEditClicked(id: Long) {
        viewModelScope.launch(handler) {
            val monster = retrieveMonsterUseCase.execute(id)
            _actions.sendAction(MonsterOverviewAction.OnEditCustomMonsterClicked(monster))
        }
    }

    override fun onDeleteClicked(id: Long) {
        viewModelScope.launch(handler + Dispatchers.Default) {
            deleteMonsterUseCase.execute(id)
            filterMonsters()
        }
    }

    override fun onLongPress(monsterId: Long) {
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
