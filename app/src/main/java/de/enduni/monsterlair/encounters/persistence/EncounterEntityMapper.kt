package de.enduni.monsterlair.encounters.persistence

import de.enduni.monsterlair.common.persistence.EncounterEntity
import de.enduni.monsterlair.common.persistence.MonsterForEncounterEntity
import de.enduni.monsterlair.encounters.domain.model.Encounter
import de.enduni.monsterlair.encounters.domain.model.EncounterMonster

class EncounterEntityMapper {

    fun toEntity(encounter: Encounter) = EncounterEntity(
        level = encounter.level,
        numberOfPlayers = encounter.numberOfPlayers,
        name = encounter.name,
        difficulty = encounter.targetDifficulty
    )

    fun toMonsterEntities(
        encounterId: Long,
        monsters: MutableList<EncounterMonster>
    ): List<MonsterForEncounterEntity> {
        return monsters.map {
            MonsterForEncounterEntity(
                monsterId = it.id,
                count = it.count,
                encounterId = encounterId
            )
        }
    }

    fun toEncounter(
        entity: EncounterEntity,
        monstersForEncounter: List<EncounterMonster>
    ) = Encounter(
        entity.id,
        entity.name,
        monstersForEncounter.toMutableList(),
        entity.level,
        entity.numberOfPlayers,
        entity.difficulty
    )


}
