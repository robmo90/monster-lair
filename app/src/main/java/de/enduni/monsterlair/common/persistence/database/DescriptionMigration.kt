package de.enduni.monsterlair.common.persistence.database

import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase

class DescriptionMigration : Migration(2, 3) {

    override fun migrate(database: SupportSQLiteDatabase) {
        database.execSQL(
            "alter table monsters add column description TEXT NOT NULL DEFAULT ''"
        )
        database.execSQL(
            "alter table hazards add column description TEXT NOT NULL DEFAULT ''"
        )
    }
}