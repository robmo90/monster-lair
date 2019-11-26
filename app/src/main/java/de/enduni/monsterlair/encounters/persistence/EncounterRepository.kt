package de.enduni.monsterlair.encounters.persistence

import de.enduni.monsterlair.common.persistence.EncounterDao
import de.enduni.monsterlair.common.persistence.EncounterEntity
import de.enduni.monsterlair.common.persistence.MonsterDao
import de.enduni.monsterlair.encounters.domain.model.Encounter
import de.enduni.monsterlair.encounters.domain.model.EncounterMonster


class EncounterRepository(
    private val encounterDao: EncounterDao,
    private val encounterMapper: EncounterEntityMapper,
    private val monsterDao: MonsterDao,
    private val monsterWithRoleMapper: MonsterWithRoleMapper
) {

    suspend fun saveEncounter(encounter: Encounter) {
        val encounterEntity = encounterMapper.toEntity(encounter)
        val encounterId = encounterDao.insertEncounter(encounterEntity)
        val monsters = encounterMapper.toMonsterEntities(encounterId, encounter.monsters)
        encounterDao.insertMonstersForEncounter(monsters)
    }

    suspend fun getEncounters(): List<Encounter> {
        return encounterDao.getAllEncounters().map {
            toDomainModel(it)
        }
    }

    suspend fun getEncounter(id: Long) = encounterDao.getEncounter(id).let { toDomainModel(it) }

    private suspend fun getMonstersEncounters(id: Long) =
        encounterDao.getAllMonstersForEncounter(encounterId = id)

    private suspend fun toDomainModel(entity: EncounterEntity): Encounter {
        val monsterForEncounterEntities = getMonstersEncounters(entity.id!!)
        val monstersForEncounter =
            monsterForEncounterEntities.map { monsterForEncounterEntity ->
                val monster = monsterDao.getMonster(monsterForEncounterEntity.monsterId)
                val monsterWithRole =
                    monsterWithRoleMapper.mapToMonsterWithRole(monster, entity.level)
                EncounterMonster(
                    monster.id!!,
                    monsterWithRole,
                    monsterForEncounterEntity.count
                )
            }
        return encounterMapper.toEncounter(entity, monstersForEncounter)
    }

}