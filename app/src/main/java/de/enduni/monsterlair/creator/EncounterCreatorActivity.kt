package de.enduni.monsterlair.creator

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.text.Html
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.widget.doAfterTextChanged
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.SimpleItemAnimator
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import de.enduni.monsterlair.R
import de.enduni.monsterlair.common.openCustomTab
import de.enduni.monsterlair.common.setTextIfNotFocused
import de.enduni.monsterlair.common.view.*
import de.enduni.monsterlair.creator.view.*
import de.enduni.monsterlair.creator.view.adapter.EncounterCreatorListAdapter
import de.enduni.monsterlair.databinding.ActivityEncounterCreatorBinding
import de.enduni.monsterlair.encounters.domain.model.EncounterDifficulty
import org.koin.androidx.viewmodel.ext.android.viewModel
import timber.log.Timber


class EncounterCreatorActivity : AppCompatActivity() {

    private lateinit var binding: ActivityEncounterCreatorBinding

    private lateinit var listAdapter: EncounterCreatorListAdapter

    private val viewModel: EncounterCreatorViewModel by viewModel()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityEncounterCreatorBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupToolbar()

        listAdapter = EncounterCreatorListAdapter(
            layoutInflater, viewModel,
            viewModel, viewModel
        )

        binding.encounterRecyclerView.adapter = listAdapter
        val animator = binding.encounterRecyclerView.itemAnimator
        if (animator is SimpleItemAnimator) {
            Timber.d("Disabling")
            animator.supportsChangeAnimations = false
        }
        viewModel.viewState.observe(this, Observer { state -> bindViewToState(state) })
        bindUi()

        viewModel.start(
            encounterName = intent.getStringExtra(EXTRA_ENCOUNTER_NAME)!!,
            numberOfPlayers = intent.getIntExtra(EXTRA_NUMBER_OF_PLAYERS, 4),
            levelOfEncounter = intent.getIntExtra(EXTRA_ENCOUNTER_LEVEL, 4),
            targetDifficulty = intent.getSerializableExtra(EXTRA_DIFFICULTY) as EncounterDifficulty?
                ?: EncounterDifficulty.TRIVIAL,
            encounterId = intent.getLongExtra(EXTRA_ENCOUNTER_ID, -1L)
        )
    }

    private fun setupToolbar() {
        setSupportActionBar(binding.toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.title = intent.getStringExtra(EXTRA_ENCOUNTER_NAME)
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        val inflater: MenuInflater = menuInflater
        inflater.inflate(R.menu.encounter_creator_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.creator_menu_edit -> {
                viewModel.onEditClicked()
                true
            }
            R.id.creator_menu_treasure_recommendation -> {
                viewModel.onTreasureRecommendationClicked()
                true
            }
            R.id.creator_menu_random_encounter -> {
                RandomEncounterDialog().show(this) { randomEncounter ->
                    viewModel.onRandomClicked(randomEncounter)
                }
                true
            }
            android.R.id.home -> {
                finish()
                true
            }
            else -> false
        }
    }

    override fun onResume() {
        super.onResume()
        viewModel.actions.observe(this, Observer { handleAction(it) })
    }

    private fun bindUi() {
        binding.searchEditText.doAfterTextChanged {
            viewModel.filterByString(it.toString())
        }

        binding.levelSlider.setOnThumbValueChangeListener { _, _, thumbIndex, value ->
            when (thumbIndex) {
                0 -> viewModel.adjustFilterLevelLower(value)
                1 -> viewModel.adjustFilterLevelUpper(value)
            }
        }
        binding.createMonster.setOnClickListener {
            CreateMonsterDialog(this, viewModel).show()
        }
        binding.withinBudgetCheckbox.setOnCheckedChangeListener { _, isChecked ->
            viewModel.adjustFilterWithinBudget(isChecked)
        }
        setupBottomSheet()
    }

    private fun setupBottomSheet() {
        val bottomSheetBehavior = BottomSheetBehavior.from(binding.filterBottomSheet)
        bottomSheetBehavior.state = BottomSheetBehavior.STATE_COLLAPSED
        bottomSheetBehavior.peekHeight =
            resources.getDimensionPixelSize(R.dimen.bottom_sheet_peek)
    }

    private fun bindViewToState(state: EncounterCreatorDisplayState) {
        listAdapter.submitList(state.list)
        binding.searchEditText.setTextIfNotFocused(state.filter?.string)
        supportActionBar?.title = state.encounterName
        state.filter?.let { filter ->
            binding.levelSliderLabel.text = getString(
                R.string.monster_level_range_values,
                filter.lowerLevel,
                filter.upperLevel
            )
            binding.levelSlider.getThumb(0).value = filter.lowerLevel
            binding.levelSlider.getThumb(1).value = filter.upperLevel
            binding.monsterTypeChips.buildMonsterTypeFilter(
                filter.monsterTypes,
                { viewModel.addMonsterTypeFilter(it) },
                { viewModel.removeMonsterTypeFilter(it) }
            )
            binding.complexityChips.buildComplexityChips(
                filter.complexities,
                { viewModel.addComplexityFilter(it) },
                { viewModel.removeComplexityFilter(it) }
            )
            binding.dangerTypeChips.buildDangerTypeChips(
                filter.dangerTypes,
                { viewModel.addDangerFilter(it) },
                { viewModel.removeDangerFilter(it) }
            )
            binding.sortByChips.buildSortByChips(
                filter.sortBy
            ) { viewModel.adjustSortBy(it) }
        }
        state.filter?.withinBudget?.let { binding.withinBudgetCheckbox.isChecked = it }
        state.error?.let {
            when (it) {
                EncounterCreatorError.RANDOM_ENCOUNTER_ERROR -> Toast.makeText(
                    this,
                    R.string.random_encounter_template_error,
                    Toast.LENGTH_LONG
                ).show()
            }
        }
    }

    private fun handleAction(action: EncounterCreatorAction?) {
        when (action) {
            is EncounterCreatorAction.EncounterSaved -> {
                finish()
            }
            is EncounterCreatorAction.DangerLinkClicked -> {
                Uri.parse(action.url).openCustomTab(this)
            }
            is EncounterCreatorAction.DangerAdded -> {
                val toast = getString(R.string.encounter_danger_added, action.name)
                Toast.makeText(this, toast, Toast.LENGTH_SHORT)
                    .show()
            }
            is EncounterCreatorAction.EditEncounterClicked -> {
                EncounterSettingDialog(
                    EncounterSettingDialog.Purpose.EDIT, settings = EncounterSettingDialog.Settings(
                        action.encounter.name,
                        action.encounter.numberOfPlayers,
                        action.encounter.level,
                        action.encounter.targetDifficulty
                    ), activity = this
                )
                    .show { result ->
                        viewModel.adjustEncounter(
                            result.encounterName,
                            result.numberOfPlayers,
                            result.encounterLevel,
                            result.encounterDifficulty
                        )
                    }
            }
            is EncounterCreatorAction.ScrollUp -> {
                binding.encounterRecyclerView.layoutManager?.scrollToPosition(0)
            }
            is EncounterCreatorAction.CustomMonsterClicked -> {
                Toast.makeText(this, R.string.custom_monster_hint, Toast.LENGTH_SHORT).show()
            }
            is EncounterCreatorAction.OnCustomMonsterPressed -> {
                EditMonsterDialog(this, action.id, action.monsterName, viewModel).show()
            }
            is EncounterCreatorAction.OnEditCustomMonsterClicked -> {
                CreateMonsterDialog(this, viewModel, action.monster).show()
            }
            is EncounterCreatorAction.OnGiveTreasureRecommendationClicked -> {
                MaterialAlertDialogBuilder(this)
                    .setTitle(R.string.treasure_recommendation)
                    .setMessage(Html.fromHtml(action.htmlTemplate, Html.FROM_HTML_MODE_COMPACT))
                    .setPositiveButton(android.R.string.ok) { _, _ -> }
                    .show()
            }
            else -> return
        }
    }

    override fun onPause() {
        super.onPause()
        viewModel.actions.removeObservers(this)
    }

    companion object {

        private const val EXTRA_ENCOUNTER_NAME = "encounterName"
        private const val EXTRA_NUMBER_OF_PLAYERS = "numberOfPlayers"
        private const val EXTRA_ENCOUNTER_LEVEL = "encounterLevel"
        private const val EXTRA_DIFFICULTY = "difficulty"
        private const val EXTRA_ENCOUNTER_ID = "encounterId"

        fun intent(
            context: Context,
            encounterName: String,
            numberOfPlayers: Int,
            encounterLevel: Int,
            difficulty: EncounterDifficulty,
            encounterId: Long = -1
        ): Intent {
            return Intent(context, EncounterCreatorActivity::class.java).apply {
                putExtra(EXTRA_ENCOUNTER_NAME, encounterName)
                putExtra(EXTRA_NUMBER_OF_PLAYERS, numberOfPlayers)
                putExtra(EXTRA_ENCOUNTER_LEVEL, encounterLevel)
                putExtra(EXTRA_DIFFICULTY, difficulty)
                putExtra(EXTRA_ENCOUNTER_ID, encounterId)
            }

        }

    }

}