package de.enduni.monsterlair

import android.app.Application
import androidx.appcompat.app.AppCompatDelegate
import de.enduni.monsterlair.common.persistence.database.DatabaseInitializer
import de.enduni.monsterlair.common.view.DarkModeManager
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.launch
import org.koin.android.ext.android.inject
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin
import timber.log.Timber

@ExperimentalCoroutinesApi
class MonsterLairApplication : Application() {

    private val databaseInitializer: DatabaseInitializer by inject()

    private val darkModeManager: DarkModeManager by inject()

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
            modules(
                listOf(
                    databaseModule,
                    hazardsModule,
                    monsterModule,
                    encounterModule,
                    treasureModule,
                    appModule
                )
            )
        }
        Timber.d("Initialized Koin")

        if (android.os.Build.VERSION.SDK_INT < 29) {
            if (darkModeManager.isDarkModeEnabled()) {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
            } else {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
            }
        }
        MainScope().launch(handler) {
            databaseInitializer.initialize()
            Timber.d("Initialized Database")
        }


    }

}