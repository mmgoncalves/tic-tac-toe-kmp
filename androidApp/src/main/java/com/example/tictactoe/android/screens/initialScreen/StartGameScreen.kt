package com.example.tictactoe.android.screens.initialScreen

import androidx.annotation.DrawableRes
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.Navigator
import cafe.adriel.voyager.navigator.currentOrThrow
import com.example.tictactoe.domain.ItemStatus
import com.example.tictactoe.domain.PlayerData
import com.example.tictactoe.android.MyApplicationTheme
import com.example.tictactoe.android.R
import com.example.tictactoe.android.screens.gameboard.GameboardScreen
import com.example.tictactoe.ui.GameUiModel

class StartGameScreen : Screen {
    @Composable
    override fun Content() {
        val navigator = LocalNavigator.currentOrThrow
        ContentBody {
            handleNavigation(it, navigator)
        }
    }

    private fun handleNavigation(selectedPlayer: ItemStatus, navigator: Navigator) {
        when (selectedPlayer) {
            ItemStatus.X -> {
                val uiModel = GameUiModel(
                    player1 = PlayerData(ItemStatus.X),
                    player2 = PlayerData(ItemStatus.O)
                )
                navigator.push(GameboardScreen(uiModel))
            }

            ItemStatus.O -> {
                val uiModel = GameUiModel(
                    player1 = PlayerData(ItemStatus.O),
                    player2 = PlayerData(ItemStatus.X)
                )
                navigator.push(GameboardScreen(uiModel))
            }

            ItemStatus.EMPTY -> {
                navigator.push(GameboardScreen())
            }
        }
    }
}

@Composable
private fun ContentBody(onClick: (ItemStatus) -> Unit) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(
                Brush.linearGradient(
                    colors = listOf(
                        MaterialTheme.colorScheme.background,
                        MaterialTheme.colorScheme.onBackground
                    )
                )
            )
    ) {

        Text(
            text = "TIC-TAC-TOE",
            modifier = Modifier
                .fillMaxWidth()
                .align(Alignment.Center),
            style = MaterialTheme.typography.displayLarge,
            color = Color.White,
            textAlign = TextAlign.Center
        )

        Column(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.Bottom
        ) {
            Text(
                text = "Escolha o primeiro jogador",
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 20.dp),
                style = MaterialTheme.typography.bodyMedium,
                color = Color.White,
                textAlign = TextAlign.Center
            )

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 50.dp),
                horizontalArrangement = Arrangement.Center
            ) {
                CardBox(
                    settings = PlayerSettings(
                        avatar = R.drawable.player_x,
                        status = ItemStatus.X
                    ),
                    onClick = onClick
                )
                CardBox(
                    settings = PlayerSettings(
                        avatar = R.drawable.player_o,
                        status = ItemStatus.O
                    ),
                    onClick = onClick
                )

            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun CardBox(settings: PlayerSettings, onClick: (ItemStatus) -> Unit) {
    Card(
        onClick = {
            onClick(settings.status)
        },
        elevation = CardDefaults.cardElevation(
            defaultElevation = 8.dp
        ),
        colors = CardDefaults.cardColors(
            containerColor = Color.White,
        ),
        shape = RoundedCornerShape(20.dp),
        modifier = Modifier
            .padding(end = 20.dp)
            .size(143.dp)

    ) {
        Image(
            painter = painterResource(id = settings.avatar),
            contentDescription = null,
            alignment = Alignment.Center,
            contentScale = ContentScale.Fit
        )
    }
}

private data class PlayerSettings(
    @DrawableRes val avatar: Int,
    val status: ItemStatus
)

@Preview
@Composable
private fun StartGamePreview() {
    MyApplicationTheme {
        ContentBody { _ -> }
    }
}