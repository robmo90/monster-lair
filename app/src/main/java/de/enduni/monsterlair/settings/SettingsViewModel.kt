package de.enduni.monsterlair.settings

import android.content.SharedPreferences
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import de.enduni.monsterlair.common.domain.Source
import de.enduni.monsterlair.common.sources.SelectSourcesDialog
import de.enduni.monsterlair.common.sources.SourceManager

class SettingsViewModel(
    private val sourceManager: SourceManager
) : ViewModel(), SelectSourcesDialog.Listener {

    val sources = MutableLiveData(sourceManager.sources)

    private var listener: SharedPreferences.OnSharedPreferenceChangeListener? = null

    fun listen() {
        listener = sourceManager.listenForChanges {
            sources.postValue(it)
        }
    }

    override fun addSource(source: Source) {
        sourceManager.sources = sourceManager.sources + source
    }

    override fun removeSource(source: Source) {
        sourceManager.sources = sourceManager.sources - source
    }

    override fun onCleared() {
        super.onCleared()
        listener?.let { sourceManager.unregister(it) }
    }
}