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
import de.enduni.monsterlair.settings.SettingsFragment
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

        updateManager.showUpdateInformationDialog(this)
    }

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