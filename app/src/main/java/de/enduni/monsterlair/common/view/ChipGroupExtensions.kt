package de.enduni.monsterlair.common.view

import com.google.android.material.chip.Chip
import com.google.android.material.chip.ChipDrawable
import com.google.android.material.chip.ChipGroup
import de.enduni.monsterlair.R
import de.enduni.monsterlair.common.domain.Complexity
import de.enduni.monsterlair.common.domain.MonsterType
import de.enduni.monsterlair.common.domain.Rarity
import de.enduni.monsterlair.common.domain.TreasureCategory
import de.enduni.monsterlair.common.getStringRes
import de.enduni.monsterlair.common.getStringResForLabel
import de.enduni.monsterlair.creator.view.DangerType
import de.enduni.monsterlair.monsters.view.SortBy

fun ChipGroup.buildMonsterTypeFilter(
    checkedTypes: List<MonsterType> = listOf(),
    onChecked: (MonsterType) -> Unit,
    onUnchecked: (MonsterType) -> Unit
) {
    this.removeAllViews()
    MonsterType.values().forEach { monsterType ->
        val chip = Chip(this.context)
        val chipDrawable = ChipDrawable.createFromAttributes(
            this.context,
            null,
            0,
            R.style.Widget_MaterialComponents_Chip_Filter
        )
        chip.setChipDrawable(chipDrawable)
        chip.text = this.context.getString(monsterType.getStringRes())
        chip.isChecked = checkedTypes.contains(monsterType)
        chip.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) {
                onChecked.invoke(monsterType)
            } else {
                onUnchecked.invoke(monsterType)
            }
        }


        this.addView(chip)
    }
}

fun ChipGroup.buildSortByChips(
    checkedSortBy: SortBy = SortBy.NAME,
    onChecked: (SortBy) -> Unit
) {
    this.isSingleSelection = true
    this.removeAllViews()
    SortBy.values().forEach { sortBy ->
        val chip = Chip(this.context)
        val chipDrawable = ChipDrawable.createFromAttributes(
            this.context,
            null,
            0,
            R.style.Widget_MaterialComponents_Chip_Choice
        )
        chip.setChipDrawable(chipDrawable)
        chip.text = this.context.getString(sortBy.getStringRes())
        chip.isChecked = checkedSortBy == sortBy
        chip.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) {
                onChecked.invoke(sortBy)
            }
        }



        this.addView(chip)
    }
}

fun ChipGroup.buildComplexityChips(
    checkedComplexities: List<Complexity> = listOf(),
    onChecked: (Complexity) -> Unit,
    onUnchecked: (Complexity) -> Unit
) {
    this.removeAllViews()
    Complexity.values().forEach { complexity ->
        val chip = Chip(this.context)
        val chipDrawable = ChipDrawable.createFromAttributes(
            this.context,
            null,
            0,
            R.style.Widget_MaterialComponents_Chip_Filter
        )
        chip.setChipDrawable(chipDrawable)
        chip.text = this.context.getString(complexity.getStringResForLabel())
        chip.isChecked = checkedComplexities.contains(complexity)
        chip.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) {
                onChecked.invoke(complexity)
            } else {
                onUnchecked.invoke(complexity)
            }
        }


        this.addView(chip)
    }
}

fun ChipGroup.buildDangerTypeChips(
    checkedDangers: List<DangerType> = listOf(),
    onChecked: (DangerType) -> Unit,
    onUnchecked: (DangerType) -> Unit
) {
    this.removeAllViews()
    DangerType.values().forEach { complexity ->
        val chip = Chip(this.context)
        val chipDrawable = ChipDrawable.createFromAttributes(
            this.context,
            null,
            0,
            R.style.Widget_MaterialComponents_Chip_Filter
        )
        chip.setChipDrawable(chipDrawable)
        chip.text = this.context.getString(complexity.getStringRes())
        chip.isChecked = checkedDangers.contains(complexity)
        chip.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) {
                onChecked.invoke(complexity)
            } else {
                onUnchecked.invoke(complexity)
            }
        }


        this.addView(chip)
    }
}

fun ChipGroup.addTreasureCategoryChips(
    categories: List<TreasureCategory>,
    clear: Boolean = false,
    removeAction: (TreasureCategory) -> Unit
) {
    if (clear) {
        this.removeAllViews()
    }
    categories.forEach { category ->
        val chip = Chip(this.context)
        val chipDrawable = ChipDrawable.createFromAttributes(
            this.context,
            null,
            0,
            R.style.Widget_MaterialComponents_Chip_Entry
        )
        chip.setChipDrawable(chipDrawable)
        chip.text = this.context.getString(category.getStringRes())
        chip.isCheckable = false
        chip.setOnCloseIconClickListener { removeAction.invoke(category) }
        this.addView(chip)
    }
}

fun ChipGroup.addTraitChips(
    traits: List<String>,
    clear: Boolean = false,
    removeAction: (String) -> Unit
) {
    if (clear) {
        this.removeAllViews()
    }
    traits.forEach { trait ->
        val chip = Chip(this.context)
        val chipDrawable = ChipDrawable.createFromAttributes(
            this.context,
            null,
            0,
            R.style.Widget_MaterialComponents_Chip_Entry
        )
        chip.setChipDrawable(chipDrawable)
        chip.text = trait
        chip.isCheckable = false
        chip.setOnCloseIconClickListener { removeAction.invoke(trait) }
        this.addView(chip)
    }
}

fun ChipGroup.addRarityChips(
    rarities: List<Rarity>,
    clear: Boolean = false,
    removeAction: (Rarity) -> Unit
) {
    if (clear) {
        this.removeAllViews()
    }
    rarities.forEach { trait ->
        val chip = Chip(this.context)
        val chipDrawable = ChipDrawable.createFromAttributes(
            this.context,
            null,
            0,
            R.style.Widget_MaterialComponents_Chip_Entry
        )
        chip.setChipDrawable(chipDrawable)
        chip.text = context.getString(trait.getStringRes())
        chip.isCheckable = false
        chip.setOnCloseIconClickListener { removeAction.invoke(trait) }
        this.addView(chip)
    }
}

fun ChipGroup.addGoldCostChips(
    lowerGoldCost: Double?,
    upperGoldCost: Double?,
    clear: Boolean = false,
    lowerRemoveAction: () -> Unit,
    upperRemoveAction: () -> Unit
) {
    if (clear) {
        this.removeAllViews()
    }
    lowerGoldCost?.let {
        val chip = Chip(this.context)
        val chipDrawable = ChipDrawable.createFromAttributes(
            this.context,
            null,
            0,
            R.style.Widget_MaterialComponents_Chip_Entry
        )
        chip.setChipDrawable(chipDrawable)
        chip.text = context.getString(R.string.lower_price_range_in_gp_chip, it)
        chip.isCheckable = false
        chip.setOnCloseIconClickListener { lowerRemoveAction.invoke() }
        this.addView(chip)
    }
    upperGoldCost?.let {
        val chip = Chip(this.context)
        val chipDrawable = ChipDrawable.createFromAttributes(
            this.context,
            null,
            0,
            R.style.Widget_MaterialComponents_Chip_Entry
        )
        chip.setChipDrawable(chipDrawable)
        chip.text = context.getString(R.string.upper_price_range_in_gp_chip, it)
        chip.isCheckable = false
        chip.setOnCloseIconClickListener { upperRemoveAction.invoke() }
        this.addView(chip)
    }
}


fun ChipGroup.setupRarityChips(
    checkedRarities: List<Rarity>,
    addAction: (Rarity) -> Unit,
    removeAction: (Rarity) -> Unit
) {
    this.removeAllViews()
    Rarity.values().forEach { rarity ->
        val chip = Chip(this.context)
        val chipDrawable = ChipDrawable.createFromAttributes(
            this.context,
            null,
            0,
            R.style.Widget_MaterialComponents_Chip_Choice
        )
        chip.setChipDrawable(chipDrawable)
        chip.text = context.getString(rarity.getStringRes())
        chip.isChecked = checkedRarities.contains(rarity)
        chip.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) {
                addAction.invoke(rarity)
            } else {
                removeAction.invoke(rarity)
            }
        }
        this.addView(chip)
    }
}