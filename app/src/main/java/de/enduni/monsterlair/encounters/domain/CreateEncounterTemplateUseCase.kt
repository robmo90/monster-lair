package de.enduni.monsterlair.encounters.domain

import android.content.Context
import com.samskivert.mustache.Mustache
import de.enduni.monsterlair.R
import de.enduni.monsterlair.common.getStringRes
import de.enduni.monsterlair.common.getXp
import de.enduni.monsterlair.encounters.domain.model.Encounter
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class CreateEncounterTemplateUseCase(
    private val context: Context,
    private val calculateEncounterBudgetUseCase: CalculateEncounterBudgetUseCase
) {

    suspend fun execute(encounter: Encounter): String =
        withContext(Dispatchers.Default) {
            val templateBytes =
                context.resources.openRawResource(R.raw.encounter_template).readBytes()
            val template = Mustache.compiler().compile(String(templateBytes))


            val budget = calculateEncounterBudgetUseCase.execute(encounter)
            val templateData = EncounterTemplateData(
                name = encounter.name,
                numberOfPlayers = encounter.numberOfPlayers,
                level = encounter.level,
                difficulty = context.getString(budget.currentDifficulty.getStringRes()),
                xp = budget.currentDifficulty.budget,
                xpAdjusted = budget.currentBudget,
                printMonsters = encounter.monsters.isNotEmpty(),
                printHazards = encounter.hazards.isNotEmpty(),
                monsters = encounter.monsters.map {
                    EncounterTemplateMonster(
                        count = it.count,
                        name = it.monster.name,
                        family = it.monster.family,
                        level = it.monster.level,
                        role = context.getString(it.monster.role.getStringRes()),
                        source = it.monster.source,
                        xp = it.monster.role.xp
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
                }
            )
            return@withContext template.execute(templateData)
        }

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
        val hazards: List<EncounterTemplateHazard>
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
