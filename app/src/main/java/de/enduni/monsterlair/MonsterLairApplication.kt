package de.enduni.monsterlair

import android.app.Application
import de.enduni.monsterlair.monsters.persistence.database.MonsterDatabaseInitializer
import kotlinx.coroutines.InternalCoroutinesApi
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.launch
import org.koin.android.ext.android.inject
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin
import timber.log.Timber

@InternalCoroutinesApi
class MonsterLairApplication : Application() {

    private val databaseInitializer: MonsterDatabaseInitializer by inject()

    override fun onCreate() {
        super.onCreate()

        if (BuildConfig.DEBUG) {
            Timber.plant(Timber.DebugTree())
        }

        startKoin {
            androidContext(this@MonsterLairApplication)
            modules(listOf(monsterModule, encounterModule))
        }
        Timber.d("Initialized Koin")

        MainScope().launch() {
            databaseInitializer.feedMonsters()
            Timber.d("Fed monsters")
        }


    }

}