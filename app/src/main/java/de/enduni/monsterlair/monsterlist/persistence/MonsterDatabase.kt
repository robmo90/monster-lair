package de.enduni.monsterlair.monsterlist.persistence

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(
    entities = [MonsterEntity::class],
    version = 1,
    exportSchema = false
)
abstract class MonsterDatabase : RoomDatabase() {

    abstract fun monsterDao(): MonsterDao


    companion object {
        fun buildDatabase(context: Context) =
            Room.databaseBuilder(context, MonsterDatabase::class.java, "MonsterDatabase")
                .fallbackToDestructiveMigration()
                .build()
    }
}