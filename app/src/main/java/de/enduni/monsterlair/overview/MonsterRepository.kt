package de.enduni.monsterlair.overview

import android.content.Context
import com.squareup.moshi.JsonAdapter
import com.squareup.moshi.Moshi
import com.squareup.moshi.Types
import de.enduni.monsterlair.R
import de.enduni.monsterlair.common.Monster
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.withContext


class MonsterRepository(private val context: Context) {

    private val jsonAdapter: JsonAdapter<List<Monster>>

    private var cachedMonsters: List<Monster>? = null

    init {
        val type = Types.newParameterizedType(List::class.java, Monster::class.java)
        jsonAdapter = Moshi.Builder().build().adapter<List<Monster>>(type)
    }

    suspend fun getMonsters() =
        flowOf(cachedMonsters?.let { it } ?: readMonstersFromAssets())


    private suspend fun readMonstersFromAssets() = withContext(Dispatchers.IO) {
        val raw = context.resources.openRawResource(R.raw.monsters)
        val json = String(raw.readBytes())
        val monsters = jsonAdapter.fromJson(json)
        cachedMonsters = monsters
        monsters ?: throw RuntimeException("Could not load monsters")
    }

}