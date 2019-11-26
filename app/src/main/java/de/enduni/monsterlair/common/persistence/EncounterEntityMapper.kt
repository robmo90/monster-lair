package de.enduni.monsterlair.common.persistence

import de.enduni.monsterlair.encounters.domain.Encounter
import de.enduni.monsterlair.encounters.domain.EncounterMonster

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


}
