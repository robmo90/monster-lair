package de.enduni.monsterlair.encounters.domain

import de.enduni.monsterlair.common.persistence.EncounterRepository
import de.enduni.monsterlair.common.persistence.MonsterRepository

class RetrieveEncountersUseCase(
    private val monsterRepository: MonsterRepository,
    private val encounterRepository: EncounterRepository,
    private val monsterWithRoleMapper: MonsterWithRoleMapper
) {


    suspend fun execute(): List<Encounter> {
        val encounterEntities = encounterRepository.getEncounters()

        return encounterEntities.map { encounter ->
            val monsterForEncounterEntities =
                encounterRepository.getMonstersEncounters(encounter.id!!)
            val monstersForEncounter =
                monsterForEncounterEntities.map { monsterForEncounterEntity ->
                    val monster = monsterRepository.getMonster(monsterForEncounterEntity.monsterId)
                    val monsterWithRole =
                        monsterWithRoleMapper.mapToMonsterWithRole(monster, encounter.level)
                    EncounterMonster(monster.id, monsterWithRole, monsterForEncounterEntity.count)
                }
            Encounter(
                encounter.id,
                encounter.name,
                monstersForEncounter.toMutableList(),
                encounter.level,
                encounter.numberOfPlayers,
                encounter.difficulty
            )
        }
    }

}