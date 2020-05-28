package de.enduni.monsterlair.common.datasource.datasource

import android.content.Context
import com.squareup.moshi.JsonAdapter
import com.squareup.moshi.Moshi
import com.squareup.moshi.Types
import de.enduni.monsterlair.R
import de.enduni.monsterlair.common.domain.MonsterTypeAdapter

class MonsterAssetDataSource(private val context: Context) :
    MonsterDataSource {

    private val jsonAdapter: JsonAdapter<List<MonsterDto>>

    init {
        val type = Types.newParameterizedType(List::class.java, MonsterDto::class.java)
        jsonAdapter = Moshi.Builder()
            .add(MonsterTypeAdapter())
            .build().adapter<List<MonsterDto>>(type)
    }

    override suspend fun getMonsters(): List<MonsterDto> {
        val raw = context.resources.openRawResource(R.raw.monsters)
        val json = String(raw.readBytes())
        val monsters = jsonAdapter.fromJson(json)
        return monsters?.let { it } ?: throw RuntimeException("Error loading monsters")
    }

    override suspend fun getMonsterUpdate(version: Long): List<MonsterDto> {
        val raw = context.resources.openRawResource(VERSIONS.getValue(version))
        val json = String(raw.readBytes())
        val monsters = jsonAdapter.fromJson(json)
        return monsters?.let { it } ?: throw RuntimeException("Error loading monsters")
    }

    companion object {

        private val VERSIONS = mapOf(
            Pair(1L, R.raw.monsters_1),
            Pair(2L, R.raw.monsters_2),
            Pair(3L, R.raw.monsters_3)
        )

    }
}