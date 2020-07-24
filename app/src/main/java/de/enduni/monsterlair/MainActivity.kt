package de.enduni.monsterlair

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentStatePagerAdapter
import de.enduni.monsterlair.databinding.ActivityMainBinding
import de.enduni.monsterlair.encounters.EncounterFragment
import de.enduni.monsterlair.hazards.HazardFragment
import de.enduni.monsterlair.monsters.MonsterFragment
import de.enduni.monsterlair.treasure.TreasureFragment
import de.enduni.monsterlair.update.UpdateManager
import org.koin.android.ext.android.inject


class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    private val updateManager: UpdateManager by inject()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.pager.adapter = MainActivityAdapter(this, supportFragmentManager)
        binding.pager.currentItem = 1
        binding.tabLayout.setupWithViewPager(binding.pager)

//        val navController = findNavController(R.id.nav_host_fragment)
//        binding.bottomNavigation.setupWithNavController(navController)
//        binding.toolbar.setupWithNavController(
//            navController, AppBarConfiguration(
//                topLevelDestinationIds = setOf(
//                    R.id.monster_overview_fragment,
//                    R.id.hazard_fragment,
//                    R.id.encounters_fragment
//                )
//            )
//        )
//
//        setupFabForDestinations(navController)

//        binding.toolbar.inflateMenu(R.menu.main_menu)
//        binding.toolbar.setOnMenuItemClickListener { item ->
//            when (item.itemId) {
//                R.id.main_menu_libraries -> {
//                    val page = String(applicationContext.assets.open("licenses.html").readBytes())
//                    navController.navigate(LicenseFragmentDirections.openLicensesAction(page))
//                    return@setOnMenuItemClickListener true
//                }
//                R.id.main_menu_content_licenses -> {
//                    val page =
//                        String(applicationContext.assets.open("other-licenses.html").readBytes())
//                    navController.navigate(LicenseFragmentDirections.openLicensesAction(page))
//                    return@setOnMenuItemClickListener true
//                }
//                else -> Timber.d("Menu item clicked but not handled $item")
//            }
//            false
//        }
//
        updateManager.showUpdateInformationDialog(this)
    }

//    private fun setupFabForDestinations(navController: NavController) {
//        navController.addOnDestinationChangedListener { controller, destination, arguments ->
//            when (destination.id) {
//                R.id.encounters_fragment -> binding.floatingActionButton.apply {
//                    setImageDrawable(getDrawable(R.drawable.ic_add))
//                    setOnClickListener {
//                        EncounterSettingDialog(
//                            EncounterSettingDialog.Purpose.CREATE,
//                            activity = this@MainActivity
//                        )
//                            .show { result ->
//                                EncounterCreatorActivity.intent(
//                                    this@MainActivity,
//                                    result.encounterName,
//                                    result.numberOfPlayers,
//                                    result.encounterLevel,
//                                    result.encounterDifficulty
//                                ).let { startActivity(it) }
//                            }
//                    }
//                    show()
//                }
//                R.id.encounterExportFragment -> binding.floatingActionButton.apply {
//                    setImageDrawable(getDrawable(R.drawable.ic_print))
//                    show()
//                }
//                else -> binding.floatingActionButton.hide()
//            }
//
//        }
//    }

    private class MainActivityAdapter(
        private val context: Context,
        fragmentManager: FragmentManager
    ) : FragmentStatePagerAdapter(
        fragmentManager,
        BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT
    ) {

        override fun getCount() = 5

        override fun getItem(position: Int): Fragment {
            return when (position) {
                0 -> SettingsFragment()
                1 -> EncounterFragment()
                2 -> MonsterFragment()
                3 -> HazardFragment()
                else -> TreasureFragment()
            }
        }

        override fun getPageTitle(position: Int): CharSequence? {
            return when (position) {
                0 -> context.getText(R.string.fragment_settings)
                1 -> context.getText(R.string.fragment_encounters)
                2 -> context.getText(R.string.fragment_monsters)
                3 -> context.getText(R.string.fragment_hazards)
                else -> context.getText(R.string.fragment_treasures)
            }
        }
    }

    companion object {


        fun intent(context: Context): Intent {
            return Intent(context, MainActivity::class.java)
        }

    }

}