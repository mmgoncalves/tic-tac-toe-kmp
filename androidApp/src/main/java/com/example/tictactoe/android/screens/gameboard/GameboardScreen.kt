package com.example.tictactoe.android.screens.gameboard

import androidx.compose.foundation.layout.Column
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import cafe.adriel.voyager.core.screen.Screen
import com.example.tictactoe.Domain.PlayerData
import com.example.tictactoe.android.MyApplicationTheme

data class GameboardScreen(
    private val firstPlayer: PlayerData,
    private val secondPlayer: PlayerData
) : Screen {

    @Composable
    override fun Content() {
        ContentBody()
        Column {
            Text("Player 1: ${firstPlayer.status.playerName}")
            Text("Player 2: ${secondPlayer.status.playerName}")
        }
    }
}

@Composable
private fun ContentBody() {
    Text("Hello gameboard")
}

@Preview
@Composable
private fun GameboardScreenPreview() {
    MyApplicationTheme {
        ContentBody()
    }
}