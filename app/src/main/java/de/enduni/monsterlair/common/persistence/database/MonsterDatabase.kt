package de.enduni.monsterlair.common.persistence.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import de.enduni.monsterlair.BuildConfig
import de.enduni.monsterlair.common.persistence.*

@Database(
    entities = [MonsterEntity::class, EncounterEntity::class, MonsterForEncounterEntity::class],
    version = 1,
    exportSchema = false
)
abstract class MonsterDatabase : RoomDatabase() {

    abstract fun monsterDao(): MonsterDao

    abstract fun encounterDao(): EncounterDao

    abstract fun hazardDao(): HazardDao


    companion object {
        fun buildDatabase(context: Context): MonsterDatabase {
            return if (BuildConfig.DEBUG) {
                Room.inMemoryDatabaseBuilder(context, MonsterDatabase::class.java)
                    .fallbackToDestructiveMigration()
                    .build()
            } else {
                Room.databaseBuilder(context, MonsterDatabase::class.java, "MonsterDatabase")
                    .fallbackToDestructiveMigration()
                    .build()
            }
        }

    }
}