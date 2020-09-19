package de.enduni.monsterlair.monsters.view.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.widget.LinearLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import coil.api.load
import de.enduni.monsterlair.R
import de.enduni.monsterlair.common.*
import de.enduni.monsterlair.common.domain.Alignment
import de.enduni.monsterlair.common.domain.Rarity
import de.enduni.monsterlair.common.domain.Size
import de.enduni.monsterlair.common.domain.Trait
import de.enduni.monsterlair.databinding.ViewholderMonsterBinding
import de.enduni.monsterlair.monsters.view.MonsterListDisplayModel


class MonsterViewHolder(
    itemView: View,
    private val monsterSelectedListener: MonsterViewHolderListener
) : RecyclerView.ViewHolder(itemView) {

    private lateinit var binding: ViewholderMonsterBinding

    private var expanded = false

    fun bind(monster: MonsterListDisplayModel) {
        expanded = false
        binding = ViewholderMonsterBinding.bind(itemView)

        binding.detailLayout.visibility = View.GONE
        binding.listItemIcon.load(
            itemView.resources.getDrawable(
                monster.type.getIcon(),
                itemView.context.theme
            )
        )
        binding.listItemTitle.text = monster.name
        binding.listItemCaption.text = monster.level.toString()

        if (binding.traits.childCount > 0) {
            binding.traits.removeAllViews()
        }
        TraitBuilder.addTraits(
            context,
            binding.traits,
            monster.rarity,
            monster.traits,
            monster.alignment,
            monster.size
        )
        binding.description.text =
            context.getString(R.string.monster_description, monster.family, monster.description)

        binding.root.setOnClickListener {
            expanded = if (expanded) {
                binding.detailLayout.collapse()
                false
            } else {
                binding.detailLayout.expand()
                true
            }
        }

        binding.archivesButton.setOnClickListener {
            monsterSelectedListener.onSelect(monsterId = monster.id)
        }
        if (monster.custom) {
            binding.root.setOnLongClickListener {
                monsterSelectedListener.onLongPress(monster.id)
                true
            }
        }
    }

    private fun addTraits(monster: MonsterListDisplayModel) {
        if (monster.rarity != Rarity.COMMON) {
            val rarity = context.getString(monster.rarity.getStringRes()).createTraitView()
            rarity.setBackgroundColor(
                if (monster.rarity == Rarity.UNCOMMON) {
                    context.getColor(R.color.trait_uncommon)
                } else {
                    context.getColor(R.color.trait_rare)
                }
            )
            binding.traits.addView(rarity)
            rarity.addMargin()
        }


        val alignment = context.getString(monster.alignment.getStringResShort()).createTraitView()
        alignment.setBackgroundColor(context.getColor(R.color.trait_alignment))
        binding.traits.addView(alignment)
        alignment.addMargin()


        val size = context.getString(monster.size.getStringRes()).createTraitView()
        size.setBackgroundColor(context.getColor(R.color.trait_size))
        binding.traits.addView(size)
        size.addMargin()

        monster.traits.forEach { trait ->
            val traitView = trait.createTraitView()
            traitView.setBackgroundColor(context.getColor(R.color.trait_general))
            binding.traits.addView(traitView)
            traitView.addMargin()
        }

    }

    fun String.createTraitView(): TextView {
        val textView = LayoutInflater.from(context).inflate(R.layout.trait, null) as TextView
        textView.text = this
        return textView
    }

    fun TextView.addMargin() {
        val params = layoutParams as LinearLayout.LayoutParams
        params.setMargins(
            0,
            0,
            context.resources.getDimensionPixelSize(R.dimen.trait_gap),
            0
        )
        layoutParams = params
    }

    val context
        get() = binding.root.context

    interface MonsterViewHolderListener {
        fun onSelect(monsterId: String)
        fun onLongPress(monsterId: String)
    }

}

object TraitBuilder {

    fun addTraits(
        context: Context,
        traitLayout: LinearLayout,
        rarity: Rarity,
        traits: List<Trait>,
        alignment: Alignment? = null,
        size: Size? = null
    ) {
        if (rarity != Rarity.COMMON) {
            val rarityView = context.getString(rarity.getStringRes()).createTraitView(context)
            rarityView.setBackgroundColor(
                if (rarity == Rarity.UNCOMMON) {
                    context.getColor(R.color.trait_uncommon)
                } else {
                    context.getColor(R.color.trait_rare)
                }
            )
            traitLayout.addView(rarityView)
            rarityView.addMargin()
        }

        if (alignment != null) {
            val alignmentView =
                context.getString(alignment.getStringResShort()).createTraitView(context)
            alignmentView.setBackgroundColor(context.getColor(R.color.trait_alignment))
            traitLayout.addView(alignmentView)
            alignmentView.addMargin()
        }

        if (size != null) {
            val sizeView = context.getString(size.getStringRes()).createTraitView(context)
            sizeView.setBackgroundColor(context.getColor(R.color.trait_size))
            traitLayout.addView(sizeView)
            sizeView.addMargin()
        }

        traits.forEach { trait ->
            val traitView = trait.createTraitView(context)
            traitView.setBackgroundColor(context.getColor(R.color.trait_general))
            traitLayout.addView(traitView)
            traitView.addMargin()
        }

    }

    private fun String.createTraitView(context: Context): TextView {
        val textView = LayoutInflater.from(context).inflate(R.layout.trait, null) as TextView
        textView.text = this
        return textView
    }

    private fun TextView.addMargin() {
        val params = layoutParams as LinearLayout.LayoutParams
        params.setMargins(
            0,
            0,
            context.resources.getDimensionPixelSize(R.dimen.trait_gap),
            0
        )
        layoutParams = params
    }

}
