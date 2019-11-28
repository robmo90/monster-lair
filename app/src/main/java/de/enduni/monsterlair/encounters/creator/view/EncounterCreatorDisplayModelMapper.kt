package de.enduni.monsterlair.encounters.creator.view

import de.enduni.monsterlair.encounters.domain.model.*

class EncounterCreatorDisplayModelMapper {

    fun toMonster(monsterWithRole: MonsterWithRole) = EncounterCreatorDisplayModel.Monster(
        id = monsterWithRole.id,
        name = monsterWithRole.name,
        level = monsterWithRole.level,
        type = monsterWithRole.type,
        family = monsterWithRole.family,
        role = monsterWithRole.role
    )

    fun toHazard(hazardWithRole: HazardWithRole) = EncounterCreatorDisplayModel.Hazard(
        id = hazardWithRole.id,
        name = hazardWithRole.name,
        level = hazardWithRole.level,
        complexity = hazardWithRole.complexity,
        role = hazardWithRole.role
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


    fun toHazardForEncounter(hazardWithRole: EncounterHazard) =
        EncounterCreatorDisplayModel.HazardForEncounter(
            id = hazardWithRole.id,
            name = hazardWithRole.hazard.name,
            level = hazardWithRole.hazard.level,
            complexity = hazardWithRole.hazard.complexity,
            role = hazardWithRole.hazard.role,
            count = hazardWithRole.count
        )
}