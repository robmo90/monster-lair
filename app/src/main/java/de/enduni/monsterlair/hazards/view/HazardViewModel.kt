package de.enduni.monsterlair.hazards.view

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import de.enduni.monsterlair.hazards.persistence.HazardRepository
import de.enduni.monsterlair.hazards.view.adapter.HazardViewHolder
import kotlinx.coroutines.launch
import timber.log.Timber

class HazardViewModel(
    private val mapper: HazardDisplayModelMapper,
    private val repository: HazardRepository
) : ViewModel(), HazardViewHolder.HazardSelectedListener {

    private val _viewState = MutableLiveData<HazardsViewState>()
    val viewState: LiveData<HazardsViewState> get() = _viewState

    init {
        viewModelScope.launch {
            val hazards = repository.getHazards()
            _viewState.postValue(HazardsViewState(
                hazards.map { mapper.toDisplayModel(it) }
            ))
        }
    }

    override fun onSelect(hazardId: Long) {
        Timber.d("Its brittney bitch $hazardId")
    }

}
