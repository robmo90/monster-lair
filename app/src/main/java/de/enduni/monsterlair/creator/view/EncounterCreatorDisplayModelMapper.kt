package de.enduni.monsterlair.creator.view

import de.enduni.monsterlair.common.getIcon
import de.enduni.monsterlair.common.getStringRes
import de.enduni.monsterlair.common.getXp
import de.enduni.monsterlair.encounters.domain.model.*

class EncounterCreatorDisplayModelMapper {

    fun toDanger(monsterWithRole: MonsterWithRole) = EncounterCreatorDisplayModel.Danger(
        type = DangerType.MONSTER,
        id = monsterWithRole.id,
        name = monsterWithRole.name,
        level = monsterWithRole.level,
        icon = monsterWithRole.type.getIcon(),
        label = monsterWithRole.family,
        xp = monsterWithRole.role.xp,
        url = monsterWithRole.url,
        originalType = monsterWithRole.type.toString()
    )

    fun toDanger(hazardWithRole: HazardWithRole) = EncounterCreatorDisplayModel.Danger(
        type = DangerType.HAZARD,
        id = hazardWithRole.id,
        name = hazardWithRole.name,
        level = hazardWithRole.level,
        icon = hazardWithRole.complexity.getIcon(),
        label = "",
        labelRes = hazardWithRole.complexity.getStringRes(),
        xp = hazardWithRole.role.getXp(hazardWithRole.complexity),
        url = hazardWithRole.url,
        originalType = hazardWithRole.complexity.toString()
    )

    fun toBudget(encounterData: Encounter) = EncounterCreatorDisplayModel.EncounterDetails(
        targetDifficulty = encounterData.targetDifficulty,
        numberOfPlayers = encounterData.numberOfPlayers,
        level = encounterData.level,
        currentBudget = encounterData.currentBudget,
        targetBudget = encounterData.targetBudget
    )

    fun toDanger(encounterMonster: EncounterMonster) =
        EncounterCreatorDisplayModel.DangerForEncounter(
            type = DangerType.MONSTER,
            id = encounterMonster.id,
            name = encounterMonster.monster.name,
            level = encounterMonster.monster.level,
            icon = encounterMonster.monster.type.getIcon(),
            label = encounterMonster.monster.family,
            xp = encounterMonster.monster.role.xp,
            role = encounterMonster.monster.role.getStringRes(),
            count = encounterMonster.count,
            url = encounterMonster.monster.url
        )

    fun toDanger(encounterHazard: EncounterHazard) =
        EncounterCreatorDisplayModel.DangerForEncounter(
            type = DangerType.HAZARD,
            id = encounterHazard.id,
            name = encounterHazard.hazard.name,
            level = encounterHazard.hazard.level,
            icon = encounterHazard.hazard.complexity.getIcon(),
            label = "",
            labelRes = encounterHazard.hazard.complexity.getStringRes(),
            xp = encounterHazard.hazard.role.getXp(encounterHazard.hazard.complexity),
            count = encounterHazard.count,
            role = encounterHazard.hazard.complexity.getStringRes(),
            url = encounterHazard.hazard.url
        )

}