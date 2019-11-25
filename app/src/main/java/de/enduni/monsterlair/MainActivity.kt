package de.enduni.monsterlair

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.findNavController
import de.enduni.monsterlair.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val navController = findNavController(R.id.nav_host_fragment)
        binding.bottomNavigation.setOnNavigationItemSelectedListener { menuItem ->
            when (menuItem.itemId) {
                R.id.navigation_monsters -> navController.navigate(R.id.monster_overview_fragment_action)
                R.id.navigation_hazards -> navController.navigate(R.id.hazard_fragment_action)
                R.id.navigation_encounters -> navController.navigate(R.id.encounters_fragment_action)
            }
            true
        }
    }

}