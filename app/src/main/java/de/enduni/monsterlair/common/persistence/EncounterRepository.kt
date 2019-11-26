package de.enduni.monsterlair.common.persistence

import de.enduni.monsterlair.encounters.domain.Encounter


class EncounterRepository(
    private val encounterDao: EncounterDao,
    private val encounterMapper: EncounterEntityMapper
) {

    suspend fun saveEncounter(encounter: Encounter) {
        val encounterEntity = encounterMapper.toEntity(encounter)
        val encounterId = encounterDao.insertEncounter(encounterEntity)
        val monsters = encounterMapper.toMonsterEntities(encounterId, encounter.monsters)
        encounterDao.insertMonstersForEncounter(monsters)
    }

    suspend fun getEncounters() = encounterDao.getAllEncounters()

    suspend fun getEncounter(id: Long) = encounterDao.getEncounter(id)

    suspend fun getMonstersEncounters(id: Long) =
        encounterDao.getAllMonstersForEncounter(encounterId = id)

}