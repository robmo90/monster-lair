package de.enduni.monsterlair.common.datasource.treasure

import android.content.Context
import com.squareup.moshi.JsonAdapter
import com.squareup.moshi.Moshi
import com.squareup.moshi.Types
import de.enduni.monsterlair.R
import de.enduni.monsterlair.common.datasource.typeadapters.EnumAdapter

class TreasureAssetDataSource(private val context: Context) :
    TreasureDataSource {

    private val jsonAdapter: JsonAdapter<List<TreasureDto>>

    init {
        val type = Types.newParameterizedType(List::class.java, TreasureDto::class.java)
        jsonAdapter = Moshi.Builder()
            .add(EnumAdapter())
            .build().adapter<List<TreasureDto>>(type)
    }

    override suspend fun getTreasures(): List<TreasureDto> {
        val raw = context.resources.openRawResource(R.raw.treasures)
        val json = String(raw.readBytes())
        val treasures = jsonAdapter.fromJson(json)
        return treasures?.let { it } ?: throw RuntimeException("Error loading treasures")
    }

}