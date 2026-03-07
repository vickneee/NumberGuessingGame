package com.example.numberguessinggame.ui

import android.R.attr.maxWidth
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.sizeIn
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.numberguessinggame.R
import com.example.numberguessinggame.model.GameViewModel
import com.example.numberguessinggame.ui.theme.NumberGuessingGameTheme

@Composable
fun GameScreen(gameViewModel: GameViewModel = viewModel()) {

    val gameUiState by gameViewModel.uiState.collectAsState()
    Scaffold(
        modifier = Modifier
            .fillMaxSize().padding(top = 16.dp),

    ) { innerPadding ->

        Column(
            modifier = Modifier
                .padding(innerPadding)
                .fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Text(
                text = stringResource(R.string.app_name),
                fontWeight = FontWeight.Bold,
                fontSize = 28.sp,
                color = MaterialTheme.colorScheme.primary
            )

            if (gameUiState.gameFinished) {
                Text(
                    text = stringResource(R.string.number_guessed_correctly, gameUiState.tryCount),
                    color = MaterialTheme.colorScheme.primary,
                    style = MaterialTheme.typography.titleMedium,
                    modifier = Modifier.padding(
                        top = 16.dp,
                        bottom = 8.dp,
                        start = 16.dp,
                        end = 16.dp
                    )
                )
            } else {
                Text(
                    stringResource(R.string.tries_result, gameUiState.tryCount),
                    modifier = Modifier.padding(
                        top = 16.dp,
                        bottom = 8.dp,
                        start = 16.dp,
                        end = 16.dp
                    )
                )
            }

            Text(
                stringResource(R.string.guess_the_number),
                style = MaterialTheme.typography.titleMedium
            )

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
                    .sizeIn(maxWidth = 400.dp)
                    .padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(20.dp)
            ) {
                Button(
                    modifier = Modifier.fillMaxWidth(),
                    onClick = { gameViewModel.checkGuess() },
                    enabled = !gameUiState.gameFinished
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
        enabled = !gameFinished,
        shape = MaterialTheme.shapes.large,
        modifier = Modifier
            .sizeIn(maxWidth = 400.dp)
            .fillMaxWidth()
            .padding(16.dp),
        singleLine = true,
        onValueChange = onUserGuessChanged,
        textStyle = TextStyle(
            color = MaterialTheme.colorScheme.onSurface,
            fontSize = 18.sp
        ),
        colors = OutlinedTextFieldDefaults.colors(
            focusedTextColor = MaterialTheme.colorScheme.onSurface,
            unfocusedTextColor = MaterialTheme.colorScheme.onSurface,
            disabledTextColor = MaterialTheme.colorScheme.onSurface,
            errorTextColor = MaterialTheme.colorScheme.onSurface,
        ),
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