package com.example.numberguessinggame

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme.colorScheme
import androidx.compose.material3.MaterialTheme.shapes
import androidx.compose.material3.MaterialTheme.typography
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.numberguessinggame.ui.theme.NumberGuessingGameTheme

@Composable
fun GameScreen(gameViewModel: GameViewModel = viewModel()) {
    val gameUiState by gameViewModel.uiState.collectAsState()

    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            text = stringResource(R.string.app_name),
            style = typography.titleLarge,
        )

        // Show success message separately when the game is finished
        if (gameUiState.gameFinished) {
            Text(
                text = stringResource(R.string.number_guessed_correctly, gameUiState.tryCount),
                color = colorScheme.primary,
                style = typography.titleMedium,
                modifier = Modifier.padding(top = 16.dp, bottom = 8.dp, start = 16.dp, end = 16.dp)
            )
        }

        Text(stringResource(R.string.guess_the_number),
            style = typography.titleMedium,
            modifier = Modifier.padding(16.dp)
        )

        Text(stringResource(R.string.tries_result, gameUiState.tryCount))

        GameLayout(
            secretNumber = gameUiState.secretNumber,
            numberInput = gameUiState.numberInput,
            tryCount = gameUiState.tryCount,
            gameFinished = gameUiState.gameFinished,
            isGuessWrong = gameUiState.isGuessWrong,
            resultResId = gameUiState.resultResId,
            onUserGuessChanged = { gameViewModel.updateUserGuess(it) },
            onKeyboardDone = { gameViewModel.checkGuess() }
        )
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(20.dp)
        ) {
            Button(
                modifier = Modifier.fillMaxWidth(),
                onClick = { gameViewModel.checkGuess() },
                enabled = !gameUiState.gameFinished // Disable the button when the game is finished
            ) {
                Text(
                    "Guess",
                    fontSize = 16.sp
                )
            }
            Button(
                modifier = Modifier.fillMaxWidth(),
                onClick = { gameViewModel.resetGame() }) {
                Text(
                    "Play Again",
                    fontSize = 16.sp
                )
            }
        }
    }
}

@Composable
fun GameLayout(
    secretNumber: Int,
    numberInput: String,
    isGuessWrong: Boolean,
    resultResId: Int,
    tryCount: Int,
    gameFinished: Boolean,
    onUserGuessChanged: (String) -> Unit = {},
    onKeyboardDone: () -> Unit = {},
) {
    OutlinedTextField(
        value = numberInput,
        enabled = !gameFinished, // Disable the input field when the game is finished
        shape = shapes.large,
        modifier = Modifier.fillMaxWidth().padding(top = 20.dp, bottom = 10.dp, start = 20.dp, end = 20.dp),
        singleLine = true,
        onValueChange = onUserGuessChanged,
        label = {
            if (isGuessWrong && resultResId != R.string.empty) {
                Text(stringResource(resultResId))
            } else {
                Text(stringResource(R.string.empty))
            }
        },
        isError = isGuessWrong,
        keyboardOptions = KeyboardOptions.Default.copy(
            imeAction = ImeAction.Done
        ),
        keyboardActions = KeyboardActions(
            onDone = { onKeyboardDone() }
        )
    )
}

@Preview(showBackground = true)
@Composable
fun GameScreenPreview() {
    NumberGuessingGameTheme {
        GameScreen()
    }
}
