package de.enduni.monsterlair.common.sources

import android.content.SharedPreferences
import androidx.core.content.edit
import de.enduni.monsterlair.common.domain.Source

class SourceManager(
    private val sharedPreferences: SharedPreferences
) {

    companion object {

        const val SOURCES = "sources"
    }

    var sources: List<Source>
        get() {
            return sharedPreferences.getStringSet(SOURCES, null)?.map { Source.valueOf(it) }
                ?: Source.values().toList()
        }
        set(sources) {
            sharedPreferences.edit {
                putStringSet(SOURCES, sources.map { it.name }.toSet())
            }
        }


    fun listenForChanges(listener: (List<Source>) -> Unit): SharedPreferences.OnSharedPreferenceChangeListener {
        val changeListener = SharedPreferences.OnSharedPreferenceChangeListener { _, key ->
            if (key == SOURCES) {
                listener.invoke(sources)
            }
        }
        sharedPreferences.registerOnSharedPreferenceChangeListener(changeListener)
        return changeListener
    }

    fun unregister(listener: SharedPreferences.OnSharedPreferenceChangeListener) {
        sharedPreferences.unregisterOnSharedPreferenceChangeListener(listener)
    }

}