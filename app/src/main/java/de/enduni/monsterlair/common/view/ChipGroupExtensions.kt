package de.enduni.monsterlair.common.view

import com.google.android.material.chip.Chip
import com.google.android.material.chip.ChipDrawable
import com.google.android.material.chip.ChipGroup
import de.enduni.monsterlair.R
import de.enduni.monsterlair.common.domain.*
import de.enduni.monsterlair.common.filter.*
import de.enduni.monsterlair.common.getStringRes
import de.enduni.monsterlair.common.getStringResForFilter
import de.enduni.monsterlair.common.view.filterchips.RemovableFilterChip
import de.enduni.monsterlair.common.view.filterchips.SelectionChip
import de.enduni.monsterlair.creator.view.DangerType

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

fun ChipGroup.buildComplexitySelection(
    checkedComplexities: List<Complexity> = listOf(),
    clear: Boolean = false,
    filterStore: ComplexityFilterStore
) {
    if (clear) {
        this.removeAllViews()
    }
    Complexity.values().forEach { complexity ->
        val chip = SelectionChip(
            this.context,
            context.getString(complexity.getStringResForFilter()),
            checkedComplexities.contains(complexity),
            { filterStore.addComplexity(complexity) },
            { filterStore.removeComplexity(complexity) }
        )
        this.addView(chip)
    }
}

fun ChipGroup.buildDangerTypeChips(
    checkedDangers: List<DangerType> = listOf(),
    onChecked: (DangerType) -> Unit,
    onUnchecked: (DangerType) -> Unit
) {
    this.removeAllViews()
    DangerType.values().forEach { dangerType ->
        val chip = SelectionChip(
            this.context,
            this.context.getString(dangerType.getStringRes()),
            checkedDangers.contains(dangerType),
            { onChecked.invoke(dangerType) },
            { onUnchecked.invoke(dangerType) }
        )
        this.addView(chip)
    }
}

fun ChipGroup.addTreasureCategoryChips(
    categories: List<TreasureCategory>,
    clear: Boolean = false,
    filterStore: TreasureCategoryFilterStore
) {
    if (clear) {
        this.removeAllViews()
    }
    categories.forEach { category ->
        val chip = RemovableFilterChip(
            this.context,
            context.getString(category.getStringRes())
        ) { filterStore.removeCategory(category) }
        this.addView(chip)
    }
}

fun ChipGroup.addTraitChips(
    traits: List<Trait>,
    clear: Boolean = false,
    filterStore: TraitFilterStore
) {
    if (clear) {
        this.removeAllViews()
    }
    traits.forEach { trait ->
        val chip = RemovableFilterChip(
            this.context,
            trait
        ) { filterStore.removeTrait(trait) }
        this.addView(chip)
    }
}

fun ChipGroup.addRarityChips(
    rarities: List<Rarity>,
    clear: Boolean = false,
    filterStore: RarityFilterStore
) {
    if (clear) {
        this.removeAllViews()
    }
    rarities.forEach { rarity ->
        val chip = RemovableFilterChip(
            this.context,
            context.getString(rarity.getStringRes())
        ) { filterStore.removeRarity(rarity) }
        this.addView(chip)
    }
}

fun ChipGroup.addComplexityChips(
    complexities: List<Complexity>,
    clear: Boolean = false,
    filterStore: ComplexityFilterStore
) {
    if (clear) {
        this.removeAllViews()
    }
    complexities.forEach { complexity ->
        val chip = RemovableFilterChip(
            this.context,
            context.getString(complexity.getStringResForFilter())
        ) { filterStore.removeComplexity(complexity) }
        this.addView(chip)
    }
}

fun ChipGroup.addMonsterTypeChips(
    monsterTypes: List<MonsterType>,
    clear: Boolean = false,
    filterStore: MonsterTypeFilterStore
) {
    if (clear) {
        this.removeAllViews()
    }
    monsterTypes.forEach { monsterType ->
        val chip = RemovableFilterChip(
            this.context,
            context.getString(monsterType.getStringRes())
        ) { filterStore.removeType(monsterType) }
        this.addView(chip)
    }
}

fun ChipGroup.addAlignmentChips(
    alignments: List<Alignment>,
    clear: Boolean = false,
    filterStore: AlignmentFilterStore
) {
    if (clear) {
        this.removeAllViews()
    }
    alignments.forEach { alignment ->
        val chip = RemovableFilterChip(
            this.context,
            context.getString(alignment.getStringRes())
        ) { filterStore.removeAlignment(alignment) }
        this.addView(chip)
    }
}

fun ChipGroup.addSizeChips(
    sizes: List<Size>,
    clear: Boolean = false,
    filterStore: SizeFilterStore
) {
    if (clear) {
        this.removeAllViews()
    }
    sizes.forEach { size ->
        val chip = RemovableFilterChip(
            this.context,
            context.getString(size.getStringRes())
        ) { filterStore.removeSize(size) }
        this.addView(chip)
    }
}

fun ChipGroup.addGoldCostChips(
    lowerGoldCost: Double?,
    upperGoldCost: Double?,
    clear: Boolean = false,
    filterStore: GoldCostFilterStore
) {
    if (clear) {
        this.removeAllViews()
    }
    lowerGoldCost?.let {
        val chip = RemovableFilterChip(
            this.context,
            context.getString(R.string.lower_price_range_in_gp_chip, it)
        ) { filterStore.setLowerGoldCost(null) }
        this.addView(chip)
    }
    upperGoldCost?.let {
        val chip = RemovableFilterChip(
            this.context,
            context.getString(R.string.upper_price_range_in_gp_chip, it)
        ) { filterStore.setLowerGoldCost(null) }
        this.addView(chip)
    }
}


fun ChipGroup.buildRaritySelection(
    checkedRarities: List<Rarity>,
    filterStore: RarityFilterStore
) {
    this.removeAllViews()
    Rarity.values().forEach { rarity ->
        val chip = SelectionChip(
            this.context,
            context.getString(rarity.getStringRes()),
            checkedRarities.contains(rarity),
            { filterStore.addRarity(rarity) },
            { filterStore.removeRarity(rarity) }
        )
        this.addView(chip)
    }
}