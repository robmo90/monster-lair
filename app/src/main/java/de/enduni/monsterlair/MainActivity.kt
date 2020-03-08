package de.enduni.monsterlair

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.NavController
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupWithNavController
import de.enduni.monsterlair.common.view.EncounterSettingDialog
import de.enduni.monsterlair.creator.EncounterCreatorActivity
import de.enduni.monsterlair.databinding.ActivityMainBinding
import de.enduni.monsterlair.update.UpdateManager
import org.koin.android.ext.android.inject
import timber.log.Timber


class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    private val updateManager: UpdateManager by inject()

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

        setupFabForDestinations(navController)

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

        updateManager.showUpdateInformationDialog(this)
    }

    private fun setupFabForDestinations(navController: NavController) {
        navController.addOnDestinationChangedListener { controller, destination, arguments ->
            when (destination.id) {
                R.id.encounters_fragment -> binding.floatingActionButton.apply {
                    setImageDrawable(getDrawable(R.drawable.ic_add))
                    setOnClickListener {
                        EncounterSettingDialog(
                            EncounterSettingDialog.Purpose.CREATE,
                            activity = this@MainActivity
                        )
                            .show { result ->
                                EncounterCreatorActivity.intent(
                                    this@MainActivity,
                                    result.encounterName,
                                    result.numberOfPlayers,
                                    result.encounterLevel,
                                    result.encounterDifficulty
                                ).let { startActivity(it) }
                            }
                    }
                    show()
                }
                R.id.encounterExportFragment -> binding.floatingActionButton.apply {
                    setImageDrawable(getDrawable(R.drawable.ic_print))
                    show()
                }
                else -> binding.floatingActionButton.hide()
            }

        }
    }

}