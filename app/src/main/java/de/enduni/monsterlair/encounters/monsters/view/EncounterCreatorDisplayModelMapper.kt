package de.enduni.monsterlair.encounters.monsters.view

import de.enduni.monsterlair.common.EncounterDifficulty
import de.enduni.monsterlair.encounters.monsters.domain.EncounterBudget
import de.enduni.monsterlair.encounters.monsters.domain.EncounterMonster
import de.enduni.monsterlair.encounters.monsters.domain.MonsterWithRole

class EncounterCreatorDisplayModelMapper {

    fun toMonster(monsterWithRole: MonsterWithRole) = EncounterCreatorDisplayModel.Monster(
        id = monsterWithRole.id,
        name = monsterWithRole.name,
        level = monsterWithRole.level,
        type = monsterWithRole.type,
        family = monsterWithRole.family,
        role = monsterWithRole.role
    )


    fun toBudget(
        encounterData: EncounterBudget,
        targetDifficulty: EncounterDifficulty
    ) = EncounterCreatorDisplayModel.EncounterInformation(
        currentDifficulty = encounterData.currentDifficulty,
        targetDifficulty = targetDifficulty,
        currentBudget = encounterData.currentBudget,
        targetBudget = encounterData.targetBudget
    )

    fun toMonsterForEncounter(monster: EncounterMonster) =
        EncounterCreatorDisplayModel.MonsterForEncounter(
            id = monster.id,
            name = monster.monster.name,
            level = monster.monster.level,
            type = monster.monster.type,
            family = monster.monster.family,
            count = monster.count,
            role = monster.monster.role
        )
}