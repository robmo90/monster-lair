package de.enduni.monsterlair.encounters.view

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import de.enduni.monsterlair.common.EncounterDifficulty

class EncounterViewModel : ViewModel() {

    private val _viewState = MutableLiveData<EncounterCreatorState>()
    val viewState: LiveData<EncounterCreatorState> get() = _viewState

    init {
        _viewState.postValue(EncounterCreatorState())
    }


    fun setLevel(levelString: String) {
        val level = levelString.toIntOrNull()
        val viewState = _viewState.value?.copy(
            levelOfPlayers = level,
            levelValid = IntRange(0, 20).contains(level)
        )
        _viewState.postValue(viewState)
    }

    fun setNumber(numberString: String) {
        val number = numberString.toIntOrNull()
        val viewState = _viewState.value?.copy(
            numberOfPlayers = number,
            numberValid = IntRange(0, 20).contains(number)
        )
        _viewState.postValue(viewState)
    }

    fun setDifficulty(difficulty: EncounterDifficulty) {
        val viewState = _viewState.value?.copy(
            difficulty = difficulty
        )
        _viewState.postValue(viewState)
    }


}