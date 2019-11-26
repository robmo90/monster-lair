package de.enduni.monsterlair.hazards.datasource

import android.content.Context
import com.squareup.moshi.JsonAdapter
import com.squareup.moshi.Moshi
import com.squareup.moshi.Types
import de.enduni.monsterlair.R
import de.enduni.monsterlair.hazards.domain.ComplexityTypeAdapter

class HazardAssetDataSource(
    private val context: Context
) : HazardDataSource {
    private val jsonAdapter: JsonAdapter<List<HazardDto>>

    init {
        val type = Types.newParameterizedType(List::class.java, HazardDto::class.java)
        jsonAdapter = Moshi.Builder()
            .add(ComplexityTypeAdapter())
            .build().adapter<List<HazardDto>>(type)
    }

    override suspend fun getHazards(): List<HazardDto> {
        val raw = context.resources.openRawResource(R.raw.hazards)
        val json = String(raw.readBytes())
        val hazards = jsonAdapter.fromJson(json)
        return hazards?.let { it } ?: throw RuntimeException("Error loading hazards")
    }
}