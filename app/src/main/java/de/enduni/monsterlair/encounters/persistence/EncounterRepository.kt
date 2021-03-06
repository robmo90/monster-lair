package de.enduni.monsterlair.encounters.persistence

import de.enduni.monsterlair.common.persistence.*
import de.enduni.monsterlair.encounters.domain.model.Encounter
import de.enduni.monsterlair.encounters.domain.model.EncounterHazard
import de.enduni.monsterlair.encounters.domain.model.EncounterMonster
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map


class EncounterRepository(
    private val encounterDao: EncounterDao,
    private val encounterMapper: EncounterEntityMapper,
    private val monsterDao: MonsterDao,
    private val hazardDao: HazardDao,
    private val monsterWithRoleMapper: MonsterWithRoleMapper,
    private val hazardWithRoleMapper: HazardWithRoleMapper
) {

    suspend fun saveEncounter(encounter: Encounter) {
        val encounterEntity = encounterMapper.toEntity(encounter)
        val encounterId = encounterDao.insertEncounter(encounterEntity)
        val monsters = encounterMapper.toMonsterEntities(encounterId, encounter.monsters)
        encounterDao.insertMonstersForEncounter(monsters)
        val hazards = encounterMapper.toHazardEntities(encounterId, encounter.hazards)
        encounterDao.insertHazardsForEncounter(hazards)
    }

    suspend fun getAllEncounters(): List<Encounter> {
        return encounterDao.getEncounters().map { toDomainModel(it) }
    }

    fun getEncounterFlow(): Flow<List<Encounter>> {
        return encounterDao.getEncounterFlow().map { entities ->
            entities.map { toDomainModel(it) }
        }
    }

    suspend fun getEncounter(id: Long) = toDomainModel(encounterDao.getEncounter(id))

    private suspend fun toDomainModel(entity: EncounterEntity): Encounter {
        val monsterForEncounterEntities = encounterDao.getAllMonstersForEncounter(entity.id!!)
        val monstersForEncounter = getMonstersForEncounter(
            monsterForEncounterEntities,
            entity.level,
            entity.useProficiencyWithoutLevel
        )
        val hazardsForEncounterEntities = encounterDao.getAllHazardsForEncounter(entity.id)
        val hazardsForEncounter = getHazardsForEncounter(
            hazardsForEncounterEntities,
            entity.level
        )
        return encounterMapper.toEncounter(entity, monstersForEncounter, hazardsForEncounter)
    }

    private suspend fun getMonstersForEncounter(
        monsterForEncounterEntities: List<MonsterForEncounterEntity>,
        level: Int,
        withoutProficiency: Boolean
    ): List<EncounterMonster> {
        return monsterForEncounterEntities.map { monsterForEncounterEntity ->
            val entity = monsterDao.getMonster(monsterForEncounterEntity.monsterId)
            val monsterWithRole =
                monsterWithRoleMapper.mapToMonsterWithRole(
                    entity.monster,
                    entity.traits,
                    level,
                    withoutProficiency
                )
            EncounterMonster(
                entity.monster.id,
                monsterWithRole,
                monsterForEncounterEntity.strength,
                monsterForEncounterEntity.count
            )
        }
    }

    private suspend fun getHazardsForEncounter(
        hazardForEncounterEntities: List<HazardForEncounterEntity>, level: Int
    ): List<EncounterHazard> {
        return hazardForEncounterEntities.map { hazardForEncounterEntity ->
            val hazard = hazardDao.getHazard(hazardForEncounterEntity.hazardId)
            val hazardWithRole =
                hazardWithRoleMapper.mapToHazardWithRole(hazard, level)
            EncounterHazard(
                hazard.hazard.id,
                hazardWithRole,
                hazardForEncounterEntity.count
            )
        }
    }

    suspend fun deleteEncounter(id: Long) {
        encounterDao.deleteEncounter(id)
    }

}