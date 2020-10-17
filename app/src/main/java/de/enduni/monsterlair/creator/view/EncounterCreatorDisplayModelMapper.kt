package de.enduni.monsterlair.creator.view

import de.enduni.monsterlair.common.domain.Strength
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
        originalType = monsterWithRole.type.toString(),
        source = monsterWithRole.sourceType,
        alignment = monsterWithRole.alignment,
        rarity = monsterWithRole.rarity,
        traits = monsterWithRole.traits,
        size = monsterWithRole.size,
        description = "${monsterWithRole.family} \u2014 ${monsterWithRole.description}",
        roleDescription = monsterWithRole.role.getStringRes()
    )

    fun toDanger(hazardWithRole: HazardWithRole) = EncounterCreatorDisplayModel.Danger(
        type = DangerType.HAZARD,
        id = hazardWithRole.id,
        name = hazardWithRole.name,
        level = hazardWithRole.level,
        icon = hazardWithRole.complexity.getIcon(),
        label = "",
        labelRes = hazardWithRole.complexity.getStringRes(),
        roleDescription = hazardWithRole.role.getStringRes(),
        xp = hazardWithRole.role.getXp(hazardWithRole.complexity),
        url = hazardWithRole.url,
        originalType = hazardWithRole.complexity.toString(),
        source = hazardWithRole.sourceType,
        alignment = null,
        rarity = hazardWithRole.rarity,
        traits = hazardWithRole.traits,
        size = null,
        description = hazardWithRole.description
    )

    fun toBudget(encounterData: Encounter) = EncounterCreatorDisplayModel.EncounterDetails(
        targetDifficulty = encounterData.targetDifficulty,
        numberOfPlayers = encounterData.numberOfPlayers,
        level = encounterData.level,
        currentBudget = encounterData.currentBudget,
        targetBudget = encounterData.targetBudget
    )

    fun toDanger(
        encounterMonster: EncounterMonster,
        encounter: Encounter
    ): EncounterCreatorDisplayModel.DangerForEncounter {
        val role = MonsterRole.determineRole(
            encounterMonster.monster.level,
            encounter.level,
            encounterMonster.strength,
            encounter.useProficiencyWithoutLevel
        )
        return EncounterCreatorDisplayModel.DangerForEncounter(
            type = DangerType.MONSTER,
            id = encounterMonster.id,
            name = encounterMonster.monster.name,
            strength = encounterMonster.strength,
            level = encounterMonster.monster.level + encounterMonster.strength.levelAdjustment,
            icon = encounterMonster.monster.type.getIcon(),
            label = encounterMonster.monster.family,
            xp = role.xp,
            role = role.getStringRes(),
            count = encounterMonster.count,
            url = encounterMonster.monster.url,
            source = encounterMonster.monster.sourceType,
            alignment = encounterMonster.monster.alignment,
            rarity = encounterMonster.monster.rarity,
            traits = encounterMonster.monster.traits,
            size = encounterMonster.monster.size,
            description = "${encounterMonster.monster.family} \u2014 ${encounterMonster.monster.description}"
        )
    }

    fun toDanger(encounterHazard: EncounterHazard) =
        EncounterCreatorDisplayModel.DangerForEncounter(
            type = DangerType.HAZARD,
            id = encounterHazard.id,
            name = encounterHazard.hazard.name,
            strength = Strength.STANDARD,
            level = encounterHazard.hazard.level,
            icon = encounterHazard.hazard.complexity.getIcon(),
            label = "",
            labelRes = encounterHazard.hazard.complexity.getStringRes(),
            xp = encounterHazard.hazard.role.getXp(encounterHazard.hazard.complexity),
            count = encounterHazard.count,
            role = encounterHazard.hazard.complexity.getStringRes(),
            url = encounterHazard.hazard.url,
            source = encounterHazard.hazard.sourceType,
            alignment = null,
            rarity = encounterHazard.hazard.rarity,
            traits = encounterHazard.hazard.traits,
            size = null,
            description = encounterHazard.hazard.description
        )

}