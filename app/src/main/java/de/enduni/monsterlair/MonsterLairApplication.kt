package de.enduni.monsterlair

import android.app.Application
import de.enduni.monsterlair.common.persistence.database.MonsterDatabaseInitializer
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.launch
import org.koin.android.ext.android.inject
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin
import timber.log.Timber

class MonsterLairApplication : Application() {

    private val databaseInitializer: MonsterDatabaseInitializer by inject()

    private val handler = CoroutineExceptionHandler { _, exception ->
        Timber.e(exception, "Caught exception")
    }

    override fun onCreate() {
        super.onCreate()

        if (BuildConfig.DEBUG) {
            Timber.plant(Timber.DebugTree())
        }

        startKoin {
            androidContext(this@MonsterLairApplication)
            modules(listOf(databaseModule, hazardsModule, monsterModule, encounterModule))
        }
        Timber.d("Initialized Koin")

        MainScope().launch(handler) {
            databaseInitializer.feedMonsters()
            Timber.d("Fed monsters, setup traps")
        }


    }

}