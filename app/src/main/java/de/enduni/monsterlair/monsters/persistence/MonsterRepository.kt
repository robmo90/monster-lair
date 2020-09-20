package de.enduni.monsterlair.monsters.persistence

import androidx.sqlite.db.SimpleSQLiteQuery
import de.enduni.monsterlair.common.domain.*
import de.enduni.monsterlair.common.persistence.*
import de.enduni.monsterlair.common.sources.SourceManager
import de.enduni.monsterlair.monsters.domain.Monster
import timber.log.Timber


class MonsterRepository(
    private val monsterDao: MonsterDao,
    private val monsterEntityMapper: MonsterEntityMapper,
    private val sourceManager: SourceManager
) {

    suspend fun getMonster(id: String) =
        monsterDao.getMonster(id).let { monsterEntityMapper.toModel(it) }

    suspend fun getMonsters(
        searchTerm: String,
        lowerLevel: Int,
        higherLevel: Int,
        sortBy: String,
        monsterTypes: List<MonsterType>,
        sizes: List<Size>,
        alignments: List<Alignment>,
        rarities: List<Rarity>,
        traits: List<Trait>
    ): List<Monster> {
        val query = buildQuery(
            searchTerm,
            monsterTypes,
            lowerLevel,
            higherLevel,
            sortBy,
            sizes,
            alignments,
            rarities
        )
        return monsterDao.getFilteredMonsters(SimpleSQLiteQuery(query))
            .toDomain()
            .filter { monster ->
                if (traits.isEmpty()) {
                    true
                } else {
                    traits.any { monster.traits.contains(it) }
                }
            }
    }

    suspend fun saveMonster(monster: Monster) {
        val entity = monsterEntityMapper.toEntity(monster)
        monsterDao.insertMonster(entity)
        monsterDao.insertTraits(monster.traits.map { MonsterTrait(it) })
        monsterDao.insertCrossRef(monster.traits.map {
            MonsterAndTraitsCrossRef(
                entity.id,
                it
            )
        })
    }

    suspend fun deleteMonster(id: String) {
        monsterDao.deleteMonster(id)
    }

    private fun buildQuery(
        filter: String?,
        monsterTypes: List<MonsterType>,
        lowerLevel: Int,
        higherLevel: Int,
        sortBy: String,
        sizes: List<Size>,
        alignments: List<Alignment>,
        rarities: List<Rarity>
    ): String {
        val filterString = if (filter.isNullOrEmpty()) "\"%\"" else "\"%${filter}%\""
        val typeFilterString = monsterTypes.buildQuery("type")
        val sizeFilterString = sizes.buildQuery("size")
        val alignmentFilterString = alignments.buildQuery("alignment")
        val rarityFilterString = rarities.buildQuery("rarity")
        val sourceFilterString = sourceManager.sources.buildQuery("sourceType")
        val query = "SELECT * FROM monsters WHERE " +
                "(name LIKE $filterString OR family LIKE $filterString) " +
                "AND level BETWEEN $lowerLevel AND $higherLevel " +
                typeFilterString +
                sizeFilterString +
                alignmentFilterString +
                rarityFilterString +
                sourceFilterString +
                "ORDER BY $sortBy ASC"
        Timber.v("Using $query")
        return query
    }


    private fun List<MonsterWithTraits>.toDomain(): List<Monster> {
        return this.map { monsterEntityMapper.toModel(it) }
    }

    suspend fun getTraits(): List<Trait> {
        return monsterDao.getTraits().map { it.name }
    }

}