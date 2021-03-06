package de.enduni.monsterlair.common.persistence.database

import android.content.Context
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase
import com.squareup.moshi.Moshi
import com.squareup.moshi.Types
import de.enduni.monsterlair.R
import de.enduni.monsterlair.common.domain.*
import timber.log.Timber
import java.util.*

class IdentifierMigration(private val context: Context) : Migration(1, 2) {

    override fun migrate(database: SupportSQLiteDatabase) {
        createNewMonsterTables(database)
        val customMonsters = migrateCustomMonster(database)
        createNewHazardTable(database)
        createNewEncounterMappingTables(database)
        val aonMapping = getAonMapping()
        migrateMonsterData(database, aonMapping, customMonsters)
        migrateHazardData(database, aonMapping)
        moveNewMonsterTable(database)
        moveNewHazardTable(database)
        createNewTreasureTables(database)
    }

    private fun createNewMonsterTables(database: SupportSQLiteDatabase) {
        database.execSQL(
            """
            CREATE TABLE IF NOT EXISTS new_monsters(
                `id` TEXT NOT NULL, 
                `name` TEXT NOT NULL, 
                `url` TEXT NOT NULL, 
                `family` TEXT NOT NULL, 
                `level` INTEGER NOT NULL, 
                `alignment` TEXT NOT NULL, 
                `type` TEXT NOT NULL, 
                `rarity` TEXT NOT NULL, 
                `size` TEXT NOT NULL, 
                `source` TEXT NOT NULL, 
                `sourceType` TEXT NOT NULL, 
                PRIMARY KEY(`id`))""".trimIndent()
        )
        database.execSQL("CREATE TABLE IF NOT EXISTS `monsterTraits` (`name` TEXT NOT NULL, PRIMARY KEY(`name`))")
        database.execSQL("CREATE TABLE IF NOT EXISTS `MonsterAndTraitsCrossRef` (`id` TEXT NOT NULL, `name` TEXT NOT NULL, PRIMARY KEY(`id`, `name`))")
        database.execSQL("CREATE INDEX IF NOT EXISTS `monster_cross_ref_index` ON `MonsterAndTraitsCrossRef` (`name`)")
    }

    private fun migrateCustomMonster(database: SupportSQLiteDatabase): List<Pair<Long, String>> {
        val cursor = database.query("SELECT * FROM monsters WHERE source LIKE \"CUSTOM_MONSTER\"")
        if (cursor.count == 0) {
            Timber.d("Found no custom monsters, returning")
            cursor.close()
            return emptyList()
        }
        Timber.d("Found ${cursor.count} monsters, let's get started")

        val newIds = mutableListOf<Pair<Long, String>>()
        cursor.moveToFirst()
        do {
            val id = cursor.getLong(cursor.getColumnIndex("id"))
            val uuid = UUID.randomUUID().toString()
            newIds.add(Pair(id, uuid))
            val name = cursor.getString(cursor.getColumnIndex("name"))
            val family = cursor.getString(cursor.getColumnIndex("family"))
            val level = cursor.getInt(cursor.getColumnIndex("level"))
            val type = cursor.getString(cursor.getColumnIndex("type"))
            val statement =
                "Insert into new_monsters (id, name, url, family, level, alignment, type, rarity, size, source, sourceType) VALUES (\"$uuid\", \"$name\", \"\", \"$family\", $level, \"${Alignment.NEUTRAL}\", \"$type\", \"${Rarity.COMMON}\", \"${Size.MEDIUM}\", \"${CustomMonster.SOURCE}\", \"${Source.CUSTOM}\")"
            Timber.d("Executing $statement")
            database.execSQL(statement)
        } while (cursor.moveToNext())
        cursor.close()
        Timber.d("These are the new IDs: $newIds")
        return newIds
    }

    private fun moveNewMonsterTable(database: SupportSQLiteDatabase) {
        database.execSQL("Drop table monsters")
        database.execSQL("Drop table monsters_for_encounters")
        database.execSQL("ALTER TABLE new_monsters RENAME TO monsters")
        database.execSQL("ALTER TABLE new_monsters_for_encounters RENAME TO monsters_for_encounters")
        database.execSQL("CREATE INDEX IF NOT EXISTS monster_for_encounter_index ON monsters_for_encounters (encounter_id)")

    }

    private fun moveNewHazardTable(database: SupportSQLiteDatabase) {
        database.execSQL("Drop table hazards")
        database.execSQL("Drop table hazards_for_encounters")
        database.execSQL("ALTER TABLE new_hazards RENAME TO hazards")
        database.execSQL("ALTER TABLE new_hazards_for_encounters RENAME TO hazards_for_encounters")
        database.execSQL("CREATE INDEX IF NOT EXISTS hazard_for_encounter_index ON hazards_for_encounters (encounter_id)")
    }

    private fun createNewHazardTable(database: SupportSQLiteDatabase) {
        database.execSQL("CREATE TABLE IF NOT EXISTS `new_hazards` (`id` TEXT NOT NULL, `name` TEXT NOT NULL, `url` TEXT NOT NULL, `level` INTEGER NOT NULL, `complexity` TEXT NOT NULL, `rarity` TEXT NOT NULL, `source` TEXT NOT NULL, `sourceType` TEXT NOT NULL, PRIMARY KEY(`id`))")
        database.execSQL("CREATE TABLE IF NOT EXISTS `hazardTraits` (`name` TEXT NOT NULL, PRIMARY KEY(`name`))")
        database.execSQL("CREATE TABLE IF NOT EXISTS `HazardsAndTraitsCrossRef` (`id` TEXT NOT NULL, `name` TEXT NOT NULL, PRIMARY KEY(`id`, `name`))")
        database.execSQL("CREATE INDEX IF NOT EXISTS `hazard_cross_ref_index` ON `HazardsAndTraitsCrossRef` (`name`)")
    }

    private fun createNewEncounterMappingTables(database: SupportSQLiteDatabase) {
        database.execSQL("ALTER TABLE encounters ADD COLUMN notes TEXT NOT NULL DEFAULT ''")
        database.execSQL("ALTER TABLE encounters ADD COLUMN useProficiencyWithoutLevel INTEGER NOT NULL DEFAULT 0")
        database.execSQL("CREATE TABLE IF NOT EXISTS new_monsters_for_encounters (`id` INTEGER, `monsterId` TEXT NOT NULL, `strength` TEXT NOT NULL, `count` INTEGER NOT NULL, `encounter_id` INTEGER NOT NULL, PRIMARY KEY(`id`), FOREIGN KEY(`encounter_id`) REFERENCES `encounters`(`id`) ON UPDATE NO ACTION ON DELETE CASCADE )")
        database.execSQL("CREATE TABLE IF NOT EXISTS new_hazards_for_encounters (`id` INTEGER, `hazardId` TEXT NOT NULL, `count` INTEGER NOT NULL, `encounter_id` INTEGER NOT NULL, PRIMARY KEY(`id`), FOREIGN KEY(`encounter_id`) REFERENCES `encounters`(`id`) ON UPDATE NO ACTION ON DELETE CASCADE )")
    }

    private fun migrateMonsterData(
        database: SupportSQLiteDatabase,
        aonMapping: Map<String, String>,
        customMonsters: List<Pair<Long, String>>
    ) {
        val monsterCursor = database.query("SELECT * FROM monsters_for_encounters")
        if (monsterCursor.count == 0) {
            Timber.d("Monster counter empty, nothing to do")
            monsterCursor.close()
            return
        }
        monsterCursor.moveToFirst()
        do {
            val id = monsterCursor.getInt(monsterCursor.getColumnIndex("id"))
            val monsterId = monsterCursor.getLong(monsterCursor.getColumnIndex("monsterId"))
            val urlCursor =
                database.query("SELECT id, url FROM monsters WHERE id is $monsterId LIMIT 1")
            urlCursor.moveToFirst()
            val url = urlCursor.getString(urlCursor.getColumnIndex("url"))
            val oldMonsterId = urlCursor.getLong(urlCursor.getColumnIndex("id"))
            urlCursor.close()
            val monsterUuid = if (url.isBlank()) {
                customMonsters.find { it.first == oldMonsterId }?.second
            } else {
                aonMapping.getValue(url)
            }
            val count = monsterCursor.getInt(monsterCursor.getColumnIndex("count"))
            val encounterId = monsterCursor.getInt(monsterCursor.getColumnIndex("encounter_id"))
            val statement =
                "Insert into new_monsters_for_encounters (id, monsterId, strength, count, encounter_id) VALUES ($id, \"$monsterUuid\", \"${Strength.STANDARD}\", $count, $encounterId)"
            Timber.d("Executing $statement")
            database.execSQL(statement)
        } while (monsterCursor.moveToNext())
    }

    private fun migrateHazardData(
        database: SupportSQLiteDatabase,
        aonMapping: Map<String, String>
    ) {
        val hazardCursor = database.query("SELECT * FROM hazards_for_encounters")
        if (hazardCursor.count == 0) {
            Timber.d("Hazard counter empty, nothing to do")
            hazardCursor.close()
            return
        }
        hazardCursor.moveToFirst()
        do {
            val id = hazardCursor.getInt(hazardCursor.getColumnIndex("id"))
            val hazardId = hazardCursor.getLong(hazardCursor.getColumnIndex("hazardId"))
            val urlCursor = database.query("SELECT url FROM hazards WHERE id is $hazardId LIMIT 1")
            urlCursor.moveToFirst()
            val url = urlCursor.getString(urlCursor.getColumnIndex("url"))
            urlCursor.close()
            val monsterUuid = aonMapping.getValue(url)
            val count = hazardCursor.getInt(hazardCursor.getColumnIndex("count"))
            val encounterId = hazardCursor.getInt(hazardCursor.getColumnIndex("encounter_id"))
            val statement =
                "Insert into new_hazards_for_encounters (id, hazardId, count, encounter_id) VALUES ($id, \"$monsterUuid\", $count, $encounterId)"
            Timber.d("Executing $statement")
            database.execSQL(statement)
        } while (hazardCursor.moveToNext())
        hazardCursor.close()
    }

    private fun createNewTreasureTables(database: SupportSQLiteDatabase) {
        database.execSQL("CREATE TABLE IF NOT EXISTS `treasures` (`id` TEXT NOT NULL, `name` TEXT NOT NULL, `url` TEXT NOT NULL, `level` INTEGER NOT NULL, `category` TEXT NOT NULL, `price` TEXT NOT NULL, `priceInGp` REAL NOT NULL, `source` TEXT NOT NULL, `sourceType` TEXT NOT NULL, `rarity` TEXT NOT NULL, PRIMARY KEY(`id`))")
        database.execSQL("CREATE TABLE IF NOT EXISTS `treasureTraits` (`name` TEXT NOT NULL, PRIMARY KEY(`name`))")
        database.execSQL("CREATE TABLE IF NOT EXISTS `TreasureAndTraitsCrossRef` (`id` TEXT NOT NULL, `name` TEXT NOT NULL, PRIMARY KEY(`id`, `name`))")
        database.execSQL("CREATE INDEX IF NOT EXISTS `treasure_cross_ref_index` ON `TreasureAndTraitsCrossRef` (`name`)")
    }


    private fun getAonMapping(): Map<String, String> {
        return context.resources.openRawResource(R.raw.mapping)
            .let { String(it.readBytes()) }
            .let { json ->
                val type = Types.newParameterizedType(
                    Map::class.java,
                    String::class.java,
                    String::class.java
                )
                Moshi.Builder().build().adapter<Map<String, String>>(type).fromJson(json)
            } ?: emptyMap()
    }
}
