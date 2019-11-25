package de.enduni.monsterlair.monsters.datasource

import android.content.Context
import com.squareup.moshi.JsonAdapter
import com.squareup.moshi.Moshi
import com.squareup.moshi.Types
import de.enduni.monsterlair.R
import de.enduni.monsterlair.monsters.domain.MonsterTypeAdapter

class MonsterAssetDataSource(private val context: Context) : MonsterDataSource {

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
}