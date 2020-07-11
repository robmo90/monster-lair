package de.enduni.monsterlair.common.persistence.database

import androidx.room.testing.MigrationTestHelper
import androidx.sqlite.db.framework.FrameworkSQLiteOpenHelperFactory
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import java.io.IOException

@RunWith(AndroidJUnit4::class)
class IdentifierMigrationTest {


    private val TEST_DB = "migration-test"

    @get:Rule
    val helper: MigrationTestHelper = MigrationTestHelper(
        InstrumentationRegistry.getInstrumentation(),
        MonsterDatabase::class.java.canonicalName,
        FrameworkSQLiteOpenHelperFactory()
    )

    @Test
    @Throws(IOException::class)
    fun migrateId() {
        var db = helper.createDatabase(TEST_DB, 1).apply {
            // db has schema version 1. insert some data using SQL queries.
            // You cannot use DAO classes because they expect the latest schema.
            val sqlStatement =
                IdentifierMigrationTest::class.java.classLoader!!.getResource("insert_data.sql")
                    .readText()
            execSQL(sqlStatement)


            close()
        }

        // Re-open the database with version 2 and provide
        // MIGRATION_1_2 as the migration process.
        db = helper.runMigrationsAndValidate(
            TEST_DB,
            2,
            true,
            IdentifierMigration(InstrumentationRegistry.getInstrumentation().targetContext)
        )

        // MigrationTestHelper automatically verifies the schema changes,
        // but you need to validate that the data was migrated properly.
        val database =
            MonsterDatabase.buildDatabase(InstrumentationRegistry.getInstrumentation().targetContext)
        GlobalScope.launch {
            val encounter = database.encounterDao().getEncounter(0)
        }

    }


}