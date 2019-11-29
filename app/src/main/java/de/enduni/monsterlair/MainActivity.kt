package de.enduni.monsterlair

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupWithNavController
import de.enduni.monsterlair.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val navController = findNavController(R.id.nav_host_fragment)
        binding.bottomNavigation.setupWithNavController(navController)
        binding.toolbar.setupWithNavController(
            navController, AppBarConfiguration(
                topLevelDestinationIds = setOf(
                    R.id.monster_overview_fragment,
                    R.id.hazard_fragment,
                    R.id.encounters_fragment
                )
            )
        )
    }

}