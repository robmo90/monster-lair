package de.enduni.monsterlair.common.view

import androidx.lifecycle.*
import de.enduni.monsterlair.common.domain.*
import de.enduni.monsterlair.monsters.domain.Monster
import de.enduni.monsterlair.monsters.domain.RetrieveMonsterUseCase
import de.enduni.monsterlair.monsters.domain.SaveMonsterUseCase
import de.enduni.monsterlair.monsters.persistence.MonsterRepository
import de.enduni.monsterlair.monsters.view.MonsterFilterStore
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.MutableStateFlow
import timber.log.Timber
import java.util.*

@ExperimentalCoroutinesApi
class CreateMonsterViewModel(
    private val retrieveMonsterUseCase: RetrieveMonsterUseCase,
    private val saveMonsterUseCase: SaveMonsterUseCase,
    private val monsterRepository: MonsterRepository,
    private val monsterFilterStore: MonsterFilterStore
) : ViewModel() {

    private val handler = CoroutineExceptionHandler { _, exception ->
        Timber.e(exception, "Caught exception")
    }

    private val _actions = ActionLiveData<CreateMonsterEvent>()
    val actions: LiveData<CreateMonsterEvent> get() = _actions

    val traits = liveData(Dispatchers.IO) {
        emit((monsterRepository.getTraits()).sorted())
    }

    private var customTraits = ""

    private val monsterFlow = MutableStateFlow(
        Monster(
            id = UUID.randomUUID().toString(),
            name = "",
            url = "",
            family = "",
            level = 0,
            alignment = Alignment.NEUTRAL,
            type = MonsterType.NONE,
            rarity = Rarity.COMMON,
            size = Size.MEDIUM,
            source = CustomMonster.SOURCE,
            sourceType = Source.CUSTOM,
            traits = emptyList(),
            description = ""
        )
    )

    val monster = monsterFlow.asLiveData()

    fun loadMonster(monsterId: String) {
        viewModelScope.launch(handler + Dispatchers.IO) {
            retrieveMonsterUseCase.execute(monsterId).let {
                Timber.d("Found Monster: $it")
                monsterFlow.value = it
            }
        }
    }

    fun changeName(name: String) {
        viewModelScope.launch(handler) { monsterFlow.value = monsterFlow.value.copy(name = name) }
    }

    fun changeFamily(family: String) {
        viewModelScope.launch(handler) {
            monsterFlow.value = monsterFlow.value.copy(family = family)
        }
    }

    fun changeLevel(level: Level) {
        viewModelScope.launch(handler) { monsterFlow.value = monsterFlow.value.copy(level = level) }
    }

    fun changeAlignment(alignment: Alignment) {
        viewModelScope.launch(handler) {
            monsterFlow.value = monsterFlow.value.copy(alignment = alignment)
        }
    }

    fun changeType(type: MonsterType) {
        viewModelScope.launch(handler) { monsterFlow.value = monsterFlow.value.copy(type = type) }
    }

    fun changeRarity(rarity: Rarity) {
        viewModelScope.launch(handler) {
            monsterFlow.value = monsterFlow.value.copy(rarity = rarity)
        }
    }

    fun changeSize(size: Size) {
        viewModelScope.launch(handler) { monsterFlow.value = monsterFlow.value.copy(size = size) }
    }

    fun addTrait(trait: Trait) {
        viewModelScope.launch(handler) {
            monsterFlow.value =
                monsterFlow.value.copy(traits = (monsterFlow.value.traits + trait).distinct())
        }
    }

    fun removeTrait(trait: Trait) {
        viewModelScope.launch(handler) {
            monsterFlow.value = monsterFlow.value.copy(traits = (monsterFlow.value.traits - trait))
        }
    }

    fun changeDescription(description: String) {
        viewModelScope.launch(handler) {
            monsterFlow.value = monsterFlow.value.copy(description = description)
        }
    }

    fun changeCustomTraits(traits: String) {
        customTraits = traits
    }

    fun saveMonster() {
        viewModelScope.launch(handler) {
            val customTraits = customTraits.split(" ").toList().filter { it.isNotEmpty() }
            val monster = monsterFlow.value.copy(
                traits = monsterFlow.value.traits + customTraits
            )
            val errors = mutableListOf<ValidationError>()
            if (monster.name.isBlank()) {
                errors.add(ValidationError.NAME)
            }
            if (!IntRange(-1, 25).contains(monster.level)) {
                errors.add(ValidationError.LEVEL)
            }
            if (errors.isNotEmpty()) {
                _actions.sendAction(CreateMonsterEvent.Error(errors))
                return@launch
            }
            withContext(Dispatchers.IO) {
                saveMonsterUseCase.execute(monster)
            }
            _actions.sendAction(CreateMonsterEvent.SavedSuccessfully)
        }
    }

}

sealed class CreateMonsterEvent {

    object SavedSuccessfully : CreateMonsterEvent()
    data class Error(val errors: List<ValidationError>) : CreateMonsterEvent()

}

enum class ValidationError {
    LEVEL,
    NAME;
}