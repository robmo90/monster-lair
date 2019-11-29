package de.enduni.monsterlair.encounters.domain

import android.content.Context
import de.enduni.monsterlair.R
import de.enduni.monsterlair.encounters.domain.model.*
import de.enduni.monsterlair.hazards.domain.Complexity
import de.enduni.monsterlair.monsters.domain.MonsterType
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.runBlocking
import org.junit.Test

class CreateEncounterTemplateUseCaseTest {

    val context = mockk<Context>()

    val useCase = CreateEncounterTemplateUseCase(
        context,
        CalculateEncounterBudgetUseCase()
    )

    @Test
    fun name() = runBlocking {
        val template = javaClass.classLoader!!.getResourceAsStream("encounter_template.html")
        every { context.resources.openRawResource(any()) } returns template
        every { context.getString(R.string.difficulty_extreme) } returns "Extreme"
        every { context.getString(any()) } returns "Some string"


        val execute =
            useCase.execute(encounter = createSampleEncounter())

        println(execute)
        Unit
    }

    private fun createSampleEncounter(): Encounter {
        return Encounter(
            id = 1L,
            name = "Roberts Encounter",
            level = 2,
            numberOfPlayers = 5,
            targetDifficulty = EncounterDifficulty.SEVERE,
            monsters = mutableListOf(
                EncounterMonster(
                    id = 2L,
                    count = 4,
                    monster = MonsterWithRole(
                        id = 2L,
                        name = "Aasimar",
                        url = "https://2e.aonprd.com/Monsters.aspx?ID=333",
                        family = "Planar Scion",
                        level = 5,
                        alignment = "Neutral Good",
                        type = MonsterType.HUMANOID,
                        size = "Medium",
                        source = "Bestiary pg. 263",
                        role = MonsterRole.LOW_LACKEY
                    )
                ),
                EncounterMonster(
                    id = 2L,
                    count = 4,
                    monster = MonsterWithRole(
                        id = 2L,
                        name = "Adamantine Golem",
                        url = "https://2e.aonprd.com/Monsters.aspx?ID=243",
                        family = "Golem",
                        level = 18,
                        alignment = "Neutral",
                        type = MonsterType.CONSTRUCT,
                        size = "Huge",
                        source = "Bestiary pg. 189",
                        role = MonsterRole.LOW_BOSS
                    )
                )
            ),
            hazards = mutableListOf(
                EncounterHazard(
                    id = 1L,
                    count = 2,
                    hazard = HazardWithRole(
                        id = 1L,
                        name = "Armageddon Orb",
                        url = "https://2e.aonprd.com/Monsters.aspx?ID=243",
                        level = 23,
                        complexity = Complexity.SIMPLE,
                        role = HazardRole.EXTREME_BOSS,
                        source = "GM Guide p. 234"
                    )
                )
            )
        )
    }
}