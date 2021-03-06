package de.enduni.monsterlair.creator.view

import androidx.lifecycle.*
import de.enduni.monsterlair.common.domain.RandomEncounterTemplate
import de.enduni.monsterlair.common.domain.SortBy
import de.enduni.monsterlair.common.domain.Strength
import de.enduni.monsterlair.common.getDefaultMaxLevel
import de.enduni.monsterlair.common.view.ActionLiveData
import de.enduni.monsterlair.creator.domain.*
import de.enduni.monsterlair.creator.view.adapter.DangerForEncounterViewHolder
import de.enduni.monsterlair.creator.view.adapter.DangerViewHolder
import de.enduni.monsterlair.creator.view.adapter.EncounterDetailViewHolder
import de.enduni.monsterlair.encounters.domain.model.*
import de.enduni.monsterlair.monsters.domain.DeleteMonsterUseCase
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import timber.log.Timber

class EncounterCreatorViewModel(
    val filterStore: EncounterCreatorFilterStore,
    private val encounterStore: EncounterStore,
    private val retrieveMonstersWithRoleUseCase: RetrieveMonstersWithRoleUseCase,
    private val retrieveHazardsWithRoleUseCase: RetrieveHazardsWithRoleUseCase,
    private val retrieveEncounterUseCase: RetrieveEncounterUseCase,
    private val createRandomEncounterUseCase: CreateRandomEncounterUseCase,
    private val deleteMonsterUseCase: DeleteMonsterUseCase,
    private val mapper: EncounterCreatorDisplayModelMapper,
    private val storeEncounterUseCase: StoreEncounterUseCase,
    private val createTreasureRecommendationUseCase: CreateTreasureRecommendationTextUseCase
) : ViewModel(),
    EncounterDetailViewHolder.ClickListener,
    DangerViewHolder.DangerListener,
    DangerForEncounterViewHolder.DangerForEncounterListener,
    AdjustMonsterStrengthDialog.Listener {

    private val _actions = ActionLiveData<EncounterCreatorAction>()
    val actions: LiveData<EncounterCreatorAction> get() = _actions

    private val handler = CoroutineExceptionHandler { _, exception ->
        Timber.e(exception, "Caught exception")
        if (exception is RandomEncounterException) {
            viewModelScope.launch {
                _actions.sendAction(EncounterCreatorAction.RandomEncounterError)
            }
        }
    }

    val filter = filterStore.filter.asLiveData()

    val encounter = liveData(handler) {
        filterStore.filter.combine(encounterStore.encounter.filter { encounter -> encounter.level != -1 && encounter.numberOfPlayers != -1 }) { filter, encounter ->
            val monsters = retrieveMonstersWithRoleUseCase.getFilteredMonsters(filter, encounter)
            val hazards = retrieveHazardsWithRoleUseCase.getFilteredHazards(filter, encounter)
            val dangers = (monsters.toMonsterDisplayModel() +
                    hazards.toHazardDisplayModel())
                .sort(filter.sortBy)

            val dangersForEncounter =
                (encounter.monsters.toEncounterMonsterDisplayModel(encounter) +
                        encounter.hazards.toEncounterHazardDisplayModel())
                    .sortedWith(compareBy({ it.name }, { it.strength }))

            val list: List<EncounterCreatorDisplayModel> =
                dangersForEncounter +
                        encounter.toBudgetDisplayModel() +
                        dangers

            EncounterCreatorDisplayState(encounterName = encounter.name, list = list)
        }
            .flowOn(Dispatchers.Default)
            .collect { emit(it) }
    }

    fun start(
        encounterName: String,
        numberOfPlayers: Int,
        levelOfEncounter: Int,
        targetDifficulty: EncounterDifficulty,
        useProficiencyWithoutLevel: Boolean,
        notes: String,
        encounterId: Long
    ) {
        if (encounterId == -1L) {
            encounterStore.setDetails(
                encounterName,
                numberOfPlayers,
                levelOfEncounter,
                targetDifficulty,
                useProficiencyWithoutLevel,
                notes
            )
            filterStore.setLowerLevel(levelOfEncounter - 5)
            filterStore.setUpperLevel(levelOfEncounter + targetDifficulty.getDefaultMaxLevel())
        } else {
            viewModelScope.launch(handler) {
                encounterStore.setup(retrieveEncounterUseCase.execute(encounterId))
                filterStore.setLowerLevel(levelOfEncounter - 5)
                filterStore.setUpperLevel(levelOfEncounter + encounterStore.value.targetDifficulty.getDefaultMaxLevel())
            }
        }
    }


    private fun List<EncounterCreatorDisplayModel.Danger>.sort(sortBy: SortBy): List<EncounterCreatorDisplayModel.Danger> {
        return when (sortBy) {
            SortBy.NAME -> this.sortedBy { it.name }
            SortBy.LEVEL -> this.sortedBy { it.level }
            SortBy.TYPE -> this.sortedBy { it.originalType }
        }
    }

    private fun List<EncounterMonster>.toEncounterMonsterDisplayModel(encounter: Encounter): List<EncounterCreatorDisplayModel.DangerForEncounter> {
        return this.map { mapper.toDanger(it, encounter) }
    }

    private fun List<EncounterHazard>.toEncounterHazardDisplayModel(): List<EncounterCreatorDisplayModel.DangerForEncounter> {
        return this.map { mapper.toDanger(it) }
    }

    private fun Encounter.toBudgetDisplayModel(): List<EncounterCreatorDisplayModel> {
        return listOf(mapper.toBudget(this))
    }

    private fun List<MonsterWithRole>.toMonsterDisplayModel(): List<EncounterCreatorDisplayModel.Danger> {
        return this.map { mapper.toDanger(it) }
    }

    private fun List<HazardWithRole>.toHazardDisplayModel(): List<EncounterCreatorDisplayModel.Danger> {
        return this.map { mapper.toDanger(it) }
    }

    override fun onDecrement(type: DangerType, id: String, strength: Strength) {
        encounterStore.decrement(type, id, strength)
    }

    override fun onIncrement(type: DangerType, id: String, strength: Strength) {
        encounterStore.increment(type, id, strength)
    }

    override fun onOpenArchiveClicked(url: String) {
        linkClicked(url)
    }

    override fun onCustomDangerDeleteClicked(type: DangerType, id: String, name: String) {
        viewModelScope.launch(handler) {
            if (type == DangerType.MONSTER) {
                _actions.sendAction(EncounterCreatorAction.CustomMonsterDelete(id, name))
            }
        }
    }

    override fun onCustomDangerEditClicked(type: DangerType, id: String) {
        viewModelScope.launch(handler) {
            if (type == DangerType.MONSTER) {
                _actions.sendAction(EncounterCreatorAction.CustomMonsterEdit(id))
            }
        }
    }

    private fun linkClicked(url: String) {
        viewModelScope.launch(handler) {
            _actions.sendAction(EncounterCreatorAction.DangerLinkClicked(url))
        }
    }

    fun onRandomClicked(randomEncounter: RandomEncounterTemplate) {
        viewModelScope.launch(handler + Dispatchers.Default) {
            val filter = filter.value!!.copy(withinBudget = false)
            val encounter = encounterStore.value
            val monsters = retrieveMonstersWithRoleUseCase.getFilteredMonsters(filter, encounter)
            val hazards = retrieveHazardsWithRoleUseCase.getFilteredHazards(filter, encounter)
            when (randomEncounter) {
                RandomEncounterTemplate.BOSS_AND_LACKEYS,
                RandomEncounterTemplate.BOSS_AND_LIEUTENANT,
                RandomEncounterTemplate.ELITE_ENEMIES -> encounterStore.adjustDifficulty(
                    EncounterDifficulty.SEVERE
                )
                RandomEncounterTemplate.LIEUTENANT_AND_LACKEYS,
                RandomEncounterTemplate.MATED_PAIR,
                RandomEncounterTemplate.TROOP -> encounterStore.adjustDifficulty(EncounterDifficulty.MODERATE)
                RandomEncounterTemplate.MOOK_SQUAD -> encounterStore.adjustDifficulty(
                    EncounterDifficulty.LOW
                )
                RandomEncounterTemplate.RANDOM -> {
                }
            }
            val monstersAndHazards = createRandomEncounterUseCase.createRandomEncounter(
                encounter,
                monsters,
                hazards,
                randomEncounter
            )
            encounterStore.swapDangers(monstersAndHazards.first, monstersAndHazards.second)
            withContext(Dispatchers.Main) {
                _actions.sendAction(EncounterCreatorAction.ScrollUp)
            }
        }
    }

    override fun onSaveClicked() {
        viewModelScope.launch(handler) {
            if (encounterStore.value.name.isBlank()) {
                encounterStore.setName(name = "Encounter")
            }
            storeEncounterUseCase.store(encounterStore.value)
            _actions.sendAction(EncounterCreatorAction.EncounterSaved)
        }
    }

    override fun onAddClicked(type: DangerType, id: String) {
        viewModelScope.launch(handler) {
            when (type) {
                DangerType.MONSTER -> {
                    val monster =
                        retrieveMonstersWithRoleUseCase.getMonster(id, encounterStore.value)
                    encounterStore.addMonster(monster)
                    _actions.postValue(EncounterCreatorAction.DangerAdded(monster.name))
                }
                DangerType.HAZARD -> {
                    val hazard =
                        retrieveHazardsWithRoleUseCase.getHazard(id, encounterStore.value)
                    encounterStore.addHazard(hazard)
                    _actions.postValue(EncounterCreatorAction.DangerAdded(hazard.name))
                }
            }
        }
    }

    fun adjustEncounter(
        encounterName: String,
        numberOfPlayers: Int,
        encounterLevel: Int,
        encounterDifficulty: EncounterDifficulty,
        useProficencyWithoutLevel: Boolean,
        notes: String
    ) {
        encounterStore.setDetails(
            encounterName,
            numberOfPlayers,
            encounterLevel,
            encounterDifficulty,
            useProficencyWithoutLevel,
            notes
        )
    }

    fun onEditClicked() {
        viewModelScope.launch(handler) {
            _actions.postValue(EncounterCreatorAction.EditEncounterClicked(encounterStore.value))
        }
    }

    fun onDeleteClicked(id: String) {
        viewModelScope.launch(handler + Dispatchers.Default) {
            deleteMonsterUseCase.execute(id)
            filterStore.refresh()
        }
    }

    fun onTreasureRecommendationClicked() {
        viewModelScope.launch(handler + Dispatchers.IO) {
            val string = createTreasureRecommendationUseCase.execute(
                encounterStore.value.level,
                encounterStore.value.numberOfPlayers
            )
            withContext(Dispatchers.Main) {
                _actions.sendAction(
                    EncounterCreatorAction.OnGiveTreasureRecommendationClicked(
                        string
                    )
                )
            }
        }
    }

    override fun onCleared() {
        super.onCleared()
        encounterStore.clear()
        filterStore.clear()
    }


    override fun onStrengthChosen(id: String, currentStrength: Strength, targetStrength: Strength) {
        encounterStore.adjustMonsterStrength(id, currentStrength, targetStrength)
    }
}

