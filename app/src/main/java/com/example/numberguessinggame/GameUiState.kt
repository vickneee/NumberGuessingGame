package com.example.numberguessinggame

data class GameUiState(
    val secretNumber: Int = (1..100).random(),
    val numberInput: String = "",
    val isGuessWrong: Boolean = false,
    val tryCount: Int = 0,
    val gameFinished: Boolean = false,
    val resultResId: Int = R.string.empty
)
