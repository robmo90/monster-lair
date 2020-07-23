package de.enduni.monsterlair.creator.view

import de.enduni.monsterlair.common.domain.Level
import de.enduni.monsterlair.common.domain.Strength
import de.enduni.monsterlair.encounters.domain.model.*
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow

class EncounterStore() {

    private val _encounterFlow = MutableStateFlow(Encounter())
    val encounter: Flow<Encounter> get() = _encounterFlow

    var value: Encounter
        get() = _encounterFlow.value
        set(value) {
            _encounterFlow.value = value
        }

    fun setup(encounter: Encounter) {
        _encounterFlow.value = encounter
    }

    fun setName(name: String) {
        value = value.copy(name = name)
    }

    fun setDetails(
        name: String,
        numberOfPlayers: Int,
        level: Level,
        difficulty: EncounterDifficulty,
        withoutProficiency: Boolean
    ) {
        value = value.copy(
            name = name,
            numberOfPlayers = numberOfPlayers,
            level = level,
            targetDifficulty = difficulty,
            withoutProficiency = withoutProficiency
        )
    }

    fun adjustDifficulty(encounterDifficulty: EncounterDifficulty) {
        value = value.copy(targetDifficulty = encounterDifficulty)
    }

    fun swapDangers(monsters: List<MonsterWithRole>, hazards: List<HazardWithRole>) {
        value = value.copy(monsters = emptyList(), hazards = emptyList())
        monsters.forEach { addMonster(it) }
        hazards.forEach { addHazard(it) }
    }

    fun addMonster(monster: MonsterWithRole) {
        val monsterAlreadyInList = value.monsters.any { it.id == monster.id }
        if (monsterAlreadyInList) {
            incrementCount(monsterId = monster.id)
        } else {
            value = value.copy(monsters = value.monsters + monster.toEncounterMonster())
        }
    }

    private fun MonsterWithRole.toEncounterMonster() = EncounterMonster(
        id = id,
        monster = this,
        count = 1,
        strength = Strength.STANDARD
    )


    fun addHazard(hazard: HazardWithRole) {
        val hazardAlreadyInList = value.hazards.any { it.id == hazard.id }
        if (hazardAlreadyInList) {
            incrementCount(hazardId = hazard.id)
        } else {
            value = value.copy(hazards = value.hazards + hazard.toEncounterHazard())
        }
    }

    private fun HazardWithRole.toEncounterHazard() = EncounterHazard(
        id = id,
        hazard = this,
        count = 1
    )

    private fun removeMonster(monsterId: String) {
        value.monsters.find { it.id == monsterId }?.let { monsterToRemove ->
            value = value.copy(
                monsters = value.monsters - monsterToRemove
            )
        }
    }

    private fun removeHazard(hazardId: String) {
        value.hazards.find { it.id == hazardId }?.let { hazardToRemove ->
            value = value.copy(
                hazards = value.hazards - hazardToRemove
            )
        }
    }

    fun increment(type: DangerType, id: String) {
        when (type) {
            DangerType.MONSTER -> incrementCount(monsterId = id)
            DangerType.HAZARD -> incrementCount(hazardId = id)
        }
    }

    fun incrementCount(monsterId: String? = null, hazardId: String? = null) {
        value.monsters.find { it.id == monsterId }?.let { monster ->
            value =
                value.copy(monsters = value.monsters - monster + monster.copy(count = monster.count + 1))
        }
        value.hazards.find { it.id == hazardId }?.let { hazard ->
            value = value.copy(
                hazards = value.hazards - hazard + hazard.copy(count = hazard.count + 1)
            )
        }
    }

    fun decrement(type: DangerType, id: String) {
        when (type) {
            DangerType.MONSTER -> decrementCount(monsterId = id)
            DangerType.HAZARD -> decrementCount(hazardId = id)
        }
    }

    fun decrementCount(monsterId: String? = null, hazardId: String? = null) {
        monsterId?.let { id ->
            value.monsters.find { it.id == monsterId }?.let { monster ->
                val count = monster.count - 1
                if (count == 0) {
                    removeMonster(id)
                } else {
                    value = value.copy(
                        monsters = value.monsters - monster + monster.copy(count = count)
                    )
                }
            }
        }
        hazardId?.let { id ->
            value.hazards.find { it.id == hazardId }?.let { hazard ->
                val count = hazard.count - 1
                if (count == 0) {
                    removeHazard(id)
                } else {
                    value = value.copy(
                        hazards = value.hazards - hazard + hazard.copy(count = count)
                    )
                }
            }
        }
    }

}