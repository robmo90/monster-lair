package de.enduni.monsterlair.encounters.creator.domain

import de.enduni.monsterlair.common.persistence.EncounterRepository
import de.enduni.monsterlair.common.persistence.MonsterRepository
import de.enduni.monsterlair.encounters.domain.Encounter
import de.enduni.monsterlair.encounters.domain.EncounterMonster
import de.enduni.monsterlair.encounters.domain.MonsterWithRoleMapper

class RetrieveEncounterUseCase(
    private val monsterRepository: MonsterRepository,
    private val encounterRepository: EncounterRepository,
    private val monsterWithRoleMapper: MonsterWithRoleMapper
) {


    suspend fun execute(id: Long): Encounter {
        val encounterEntities = encounterRepository.getEncounter(id)

        return encounterEntities.let { encounter ->
            val monsterForEncounterEntities =
                encounterRepository.getMonstersEncounters(encounter.id!!)
            val monstersForEncounter =
                monsterForEncounterEntities.map { monsterForEncounterEntity ->
                    val monster = monsterRepository.getMonster(monsterForEncounterEntity.monsterId)
                    val monsterWithRole =
                        monsterWithRoleMapper.mapToMonsterWithRole(monster, encounter.level)
                    EncounterMonster(
                        monster.id,
                        monsterWithRole,
                        monsterForEncounterEntity.count
                    )
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