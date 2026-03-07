package com.example.numberguessinggame.model

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import com.example.numberguessinggame.ui.GameUiState
import com.example.numberguessinggame.R
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

class GameViewModel : ViewModel() {

    private val _uiState = MutableStateFlow(GameUiState())
    val uiState: StateFlow<GameUiState> = _uiState.asStateFlow()

    var userGuess by mutableStateOf("")
        private set

    fun checkGuess() {
        val guessedNumber = userGuess.toIntOrNull()
        if (guessedNumber != null) {
            _uiState.update { currentState ->
                val isCorrect = guessedNumber == currentState.secretNumber
                val resultResId = when {
                    isCorrect -> R.string.number_guessed_correctly
                    guessedNumber < currentState.secretNumber -> R.string.too_low
                    else -> R.string.too_high
                }
                currentState.copy(
                    isGuessWrong = !isCorrect,
                    tryCount = currentState.tryCount + 1,
                    gameFinished = isCorrect,
                    resultResId = resultResId
                )
            }
        } else {
            _uiState.update { it.copy(isGuessWrong = true, resultResId = R.string.wrong_guess) }
        }
    }

    fun updateUserGuess(guess: String) {
        userGuess = guess
        _uiState.update { currentState ->
            currentState.copy(numberInput = guess, isGuessWrong = false)
        }
    }

    fun resetGame() {
        userGuess = ""
        _uiState.value = GameUiState()
    }
}