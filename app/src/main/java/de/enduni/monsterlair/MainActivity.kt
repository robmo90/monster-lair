package de.enduni.monsterlair

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupWithNavController
import de.enduni.monsterlair.databinding.ActivityMainBinding
import timber.log.Timber


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
        binding.toolbar.inflateMenu(R.menu.main_menu)
        binding.toolbar.setOnMenuItemClickListener { item ->
            when (item.itemId) {
                R.id.main_menu_libraries -> {
                    val page = String(applicationContext.assets.open("licenses.html").readBytes())
                    navController.navigate(LicenseFragmentDirections.openLicensesAction(page))
                    return@setOnMenuItemClickListener true
                }
                R.id.main_menu_content_licenses -> {
                    val page =
                        String(applicationContext.assets.open("other-licenses.html").readBytes())
                    navController.navigate(LicenseFragmentDirections.openLicensesAction(page))
                    return@setOnMenuItemClickListener true
                }
                else -> Timber.d("Menu item clicked but not handled $item")
            }
            false
        }
    }

}