package de.enduni.monsterlair

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityOptionsCompat
import de.enduni.monsterlair.common.persistence.database.DatabaseInitializer
import de.enduni.monsterlair.databinding.ActivitySplashscreenBinding
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import org.koin.android.ext.android.inject

class SplashScreenActivity : AppCompatActivity() {

    private lateinit var binding: ActivitySplashscreenBinding

    private val databaseInitializer: DatabaseInitializer by inject()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySplashscreenBinding.inflate(layoutInflater)
        setContentView(binding.root)

        MainScope().launch {
            databaseInitializer.migrationRunning
                .collect { migrationRunning ->
                    if (migrationRunning) {
                        binding.updateGroup.visibility = View.VISIBLE
                    } else {
                        startActivity(
                            MainActivity.intent(this@SplashScreenActivity),
                            ActivityOptionsCompat.makeCustomAnimation(
                                this@SplashScreenActivity,
                                android.R.anim.fade_in,
                                android.R.anim.fade_out
                            ).toBundle()
                        )
                    }
                }
        }
    }
}