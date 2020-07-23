package de.enduni.monsterlair.encounters.persistence

import de.enduni.monsterlair.common.persistence.EncounterEntity
import de.enduni.monsterlair.common.persistence.HazardForEncounterEntity
import de.enduni.monsterlair.common.persistence.MonsterForEncounterEntity
import de.enduni.monsterlair.encounters.domain.model.Encounter
import de.enduni.monsterlair.encounters.domain.model.EncounterHazard
import de.enduni.monsterlair.encounters.domain.model.EncounterMonster

class EncounterEntityMapper {

    fun toEntity(encounter: Encounter) = EncounterEntity(
        id = encounter.id,
        level = encounter.level,
        numberOfPlayers = encounter.numberOfPlayers,
        name = encounter.name,
        difficulty = encounter.targetDifficulty,
        notes = "",
        withoutProficiency = false
    )

    fun toMonsterEntities(
        encounterId: Long,
        monsters: List<EncounterMonster>
    ): List<MonsterForEncounterEntity> {
        return monsters.map {
            MonsterForEncounterEntity(
                monsterId = it.id,
                count = it.count,
                encounterId = encounterId,
                strength = it.strength
            )
        }
    }

    fun toHazardEntities(
        encounterId: Long,
        hazards: List<EncounterHazard>
    ): List<HazardForEncounterEntity> {
        return hazards.map {
            HazardForEncounterEntity(
                hazardId = it.id,
                count = it.count,
                encounterId = encounterId
            )
        }
    }

    fun toEncounter(
        entity: EncounterEntity,
        monstersForEncounter: List<EncounterMonster>,
        hazardsForEncounter: List<EncounterHazard>
    ) = Encounter(
        entity.id,
        entity.name,
        monstersForEncounter.toMutableList(),
        hazardsForEncounter.toMutableList(),
        entity.level,
        entity.numberOfPlayers,
        entity.difficulty
    )


}
