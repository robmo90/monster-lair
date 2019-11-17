package de.enduni.monsterlair

import android.app.Application
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin

class MonsterLairApplication : Application() {

    override fun onCreate(){
        super.onCreate()

        startKoin {
            androidContext(this@MonsterLairApplication)
            modules(myModule)
        }
    }

}