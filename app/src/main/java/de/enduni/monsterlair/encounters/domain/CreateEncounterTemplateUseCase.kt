package de.enduni.monsterlair.encounters.domain

import android.content.Context
import com.samskivert.mustache.Mustache
import de.enduni.monsterlair.R
import de.enduni.monsterlair.common.domain.CustomMonster
import de.enduni.monsterlair.common.domain.Strength
import de.enduni.monsterlair.common.getStringRes
import de.enduni.monsterlair.common.getXp
import de.enduni.monsterlair.encounters.domain.model.Encounter
import de.enduni.monsterlair.encounters.domain.model.EncounterMonster
import de.enduni.monsterlair.encounters.domain.model.MonsterRole
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class CreateEncounterTemplateUseCase(
    private val context: Context
) {

    suspend fun execute(encounter: Encounter): String =
        withContext(Dispatchers.Default) {
            val templateBytes =
                context.resources.openRawResource(R.raw.encounter_template).readBytes()
            val template = Mustache.compiler().compile(String(templateBytes))


            val templateData = EncounterTemplateData(
                name = encounter.name,
                numberOfPlayers = encounter.numberOfPlayers,
                level = encounter.level,
                difficulty = context.getString(encounter.currentDifficulty.getStringRes()),
                xp = encounter.currentDifficulty.budget,
                xpAdjusted = encounter.currentBudget,
                printMonsters = encounter.monsters.isNotEmpty(),
                printHazards = encounter.hazards.isNotEmpty(),
                monsters = encounter.monsters.map {
                    val name = when (it.strength) {
                        Strength.STANDARD -> it.monster.name
                        Strength.ELITE -> context.getString(
                            R.string.elite_name_template,
                            it.monster.name
                        )
                        Strength.WEAK -> context.getString(
                            R.string.weak_name_template,
                            it.monster.name
                        )
                    }
                    EncounterTemplateMonster(
                        count = it.count,
                        name = name,
                        family = it.monster.family,
                        level = it.monster.level + it.strength.levelAdjustment,
                        role = context.getString(it.monster.role.getStringRes()),
                        source = getSource(it),
                        xp = MonsterRole.determineRole(
                            monsterLevel = it.monster.level,
                            encounterLevel = encounter.level,
                            strength = it.strength,
                            withoutProficiency = encounter.useProficiencyWithoutLevel
                        ).xp
                    )
                },
                hazards = encounter.hazards.map {
                    EncounterTemplateHazard(
                        count = it.count,
                        name = it.hazard.name,
                        level = it.hazard.level,
                        role = context.getString(it.hazard.role.getStringRes()),
                        source = it.hazard.source,
                        xp = it.hazard.role.getXp(it.hazard.complexity)
                    )
                },
                hasNotes = encounter.notes.isNotBlank(),
                notes = encounter.notes
            )
            return@withContext template.execute(templateData)
        }

    private fun getSource(it: EncounterMonster) =
        if (it.monster.source == CustomMonster.SOURCE) "Custom Monster" else it.monster.source

    data class EncounterTemplateData(
        val name: String,
        val numberOfPlayers: Int,
        val level: Int,
        val difficulty: String,
        val xp: Int,
        val xpAdjusted: Int,
        val printMonsters: Boolean,
        val monsters: List<EncounterTemplateMonster>,
        val printHazards: Boolean,
        val hazards: List<EncounterTemplateHazard>,
        val hasNotes: Boolean,
        val notes: String
    )

    data class EncounterTemplateMonster(
        val count: Int,
        val name: String,
        val family: String,
        val level: Int,
        val role: String,
        val source: String,
        val xp: Int
    )

    data class EncounterTemplateHazard(
        val count: Int,
        val name: String,
        val level: Int,
        val role: String,
        val source: String,
        val xp: Int
    )

}
