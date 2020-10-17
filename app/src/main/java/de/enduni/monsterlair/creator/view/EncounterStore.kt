package de.enduni.monsterlair.creator.view

import de.enduni.monsterlair.common.domain.Level
import de.enduni.monsterlair.common.domain.Strength
import de.enduni.monsterlair.encounters.domain.model.*
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import timber.log.Timber

class EncounterStore {

    private val _encounterFlow = MutableStateFlow(Encounter())
    val encounter: StateFlow<Encounter> get() = _encounterFlow

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
        useProficiencyWithoutLevel: Boolean,
        notes: String
    ) {
        value = value.copy(
            name = name,
            numberOfPlayers = numberOfPlayers,
            level = level,
            targetDifficulty = difficulty,
            useProficiencyWithoutLevel = useProficiencyWithoutLevel,
            notes = notes
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
        val monsterAlreadyInList =
            value.monsters.any { it.id == monster.id && it.strength == Strength.STANDARD }
        if (monsterAlreadyInList) {
            incrementMonsterCount(monsterId = monster.id, strength = Strength.STANDARD)
        } else {
            value = value.copy(monsters = value.monsters + monster.toEncounterMonster())
        }
    }

    private fun MonsterWithRole.toEncounterMonster(strength: Strength = Strength.STANDARD) =
        EncounterMonster(
            id = id,
            monster = this,
            count = 1,
            strength = strength
        )


    fun addHazard(hazard: HazardWithRole) {
        val hazardAlreadyInList = value.hazards.any { it.id == hazard.id }
        if (hazardAlreadyInList) {
            incrementHazardCount(hazardId = hazard.id)
        } else {
            value = value.copy(hazards = value.hazards + hazard.toEncounterHazard())
        }
    }

    private fun HazardWithRole.toEncounterHazard() = EncounterHazard(
        id = id,
        hazard = this,
        count = 1
    )

    private fun removeMonster(monsterId: String, strength: Strength) {
        findMonster(monsterId, strength) { monsterToRemove ->
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

    fun increment(type: DangerType, id: String, strength: Strength) {
        when (type) {
            DangerType.MONSTER -> incrementMonsterCount(id, strength)
            DangerType.HAZARD -> incrementHazardCount(id)
        }
    }

    private fun incrementMonsterCount(monsterId: String, strength: Strength) {
        findMonster(monsterId, strength) { monster ->
            value =
                value.copy(monsters = value.monsters - monster + monster.copy(count = monster.count + 1))
        }
    }

    private fun incrementHazardCount(hazardId: String) {
        findHazardWithId(hazardId) { hazard ->
            value = value.copy(
                hazards = value.hazards - hazard + hazard.copy(count = hazard.count + 1)
            )
        }
    }

    private fun findMonster(
        id: String,
        strength: Strength,
        action: (EncounterMonster) -> Unit
    ) {
        value.monsters.find { it.id == id && it.strength == strength }?.let(action)
    }

    private fun findHazardWithId(id: String, action: (EncounterHazard) -> Unit) {
        value.hazards.find { it.id == id }?.let(action)
    }

    fun decrement(type: DangerType, id: String, strength: Strength) {
        when (type) {
            DangerType.MONSTER -> decrementMonsterCount(id, strength)
            DangerType.HAZARD -> decrementHazardCount(id)
        }
    }

    private fun decrementMonsterCount(monsterId: String, strength: Strength) {
        findMonster(monsterId, strength) { monster ->
            val count = monster.count - 1
            if (count == 0) {
                removeMonster(monsterId, strength)
            } else {
                value = value.copy(
                    monsters = value.monsters - monster + monster.copy(count = count)
                )
            }
        }
    }

    private fun decrementHazardCount(hazardId: String) {
        findHazardWithId(hazardId) { hazard ->
            val count = hazard.count - 1
            if (count == 0) {
                removeHazard(hazardId)
            } else {
                value = value.copy(
                    hazards = value.hazards - hazard + hazard.copy(count = count)
                )
            }
        }
    }

    fun clear() {
        value = Encounter()
    }

    fun adjustMonsterStrength(id: String, currentStrength: Strength, targetStrength: Strength) {
        findMonster(id, currentStrength) { monster ->
            Timber.d("Adjusting strength of $id from $currentStrength to $targetStrength")
            Timber.d("Current Monsters: ${value.monsters.joinToString { "${it.count} ${it.strength} ${it.monster.name}" }}")
            decrementMonsterCount(id, currentStrength)
            Timber.d("Monsters after decrementing: ${value.monsters.joinToString { "${it.count} ${it.strength} ${it.monster.name}" }}")
            val monsterWithStrengthAlreadyInList =
                value.monsters.any { it.id == id && it.strength == targetStrength }
            Timber.d("Found monster with strength in list: $monsterWithStrengthAlreadyInList")
            if (monsterWithStrengthAlreadyInList) {
                incrementMonsterCount(id, targetStrength)
            } else {
                value = value.copy(
                    monsters = value.monsters + monster.monster.toEncounterMonster(targetStrength)
                )
            }
            Timber.d("Monsters after operation: ${value.monsters.joinToString { "${it.count} ${it.strength} ${it.monster.name}" }}")
        }
    }

}