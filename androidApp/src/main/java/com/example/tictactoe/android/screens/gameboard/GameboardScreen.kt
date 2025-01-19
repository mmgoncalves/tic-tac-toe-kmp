package com.example.tictactoe.android.screens.gameboard

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
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
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import cafe.adriel.voyager.core.screen.Screen
import com.example.tictactoe.Domain.ColumnId
import com.example.tictactoe.Domain.GameData
import com.example.tictactoe.Domain.GameDataFactory
import com.example.tictactoe.Domain.ItemId
import com.example.tictactoe.Domain.ItemStatus
import com.example.tictactoe.Domain.PlayerData
import com.example.tictactoe.android.MyApplicationTheme
import com.example.tictactoe.android.helper.sideBorder
import com.example.tictactoe.ui.GameUiModel

data class GameboardScreen(
    private val firstPlayer: PlayerData,
    private val secondPlayer: PlayerData,
    private val gameUiModel: GameUiModel = GameUiModel()
) : Screen {

    @Composable
    override fun Content() {
        val model by remember { mutableStateOf(gameUiModel) }
        val state by model.state.collectAsState()

        ContentBody(
            data = state,
            onAction = {_, _ -> },
            restartAction = { }
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun ContentBody(
    data: GameData,
    onAction: (ColumnId, ItemId) -> Unit,
    restartAction: () -> Unit
) {
    val size = LocalConfiguration.current.screenWidthDp.dp - 40.dp
    var isEnable by remember { mutableStateOf(false) }
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
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 20.dp)
                .padding(vertical = 150.dp)
        ) {
            Text(
                text = "Vez do jogador O",
                modifier = Modifier.fillMaxWidth()
                    .padding(bottom = 100.dp),
                textAlign = TextAlign.Center,
                style = MaterialTheme.typography.bodyLarge,
                color = Color.White
            )

            Card(
                enabled = isEnable,
                onClick = {},
                elevation = CardDefaults.cardElevation(
                    defaultElevation = 8.dp
                ),
                colors = CardDefaults.cardColors(
                    containerColor = Color.White,
                    disabledContainerColor = Color.DarkGray.copy(alpha = 0.1f),

                ),
                shape = RoundedCornerShape(20.dp),
                modifier = Modifier.size(size)
            ) {
                DrawColumn(data, isEnable, onAction, restartAction)
            }
        }

        Card(
            onClick = { isEnable = isEnable.not()},
            colors = CardDefaults.cardColors(
                containerColor = Color.Transparent
            ),
            modifier = Modifier
                .align(alignment = Alignment.BottomCenter)
                .padding(bottom = 40.dp)
                .border(
                    width = 1.dp,
                    color = Color.White,
                    shape = RoundedCornerShape(40.dp)
                )
        ) {
            Text(
                text = "Novo Jogo",
                style = MaterialTheme.typography.bodyMedium,
                color = Color.White,
                modifier = Modifier
                    .padding(all = 20.dp)
            )
        }
    }
}

@Composable
private fun DrawColumn(
    data: GameData,
    isEnable: Boolean,
    onAction: (ColumnId, ItemId) -> Unit,
    restartAction: () -> Unit
) {
    Column(
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.fillMaxSize()
    ) {
        data.columns.forEach {
            DrawRow(
                columnId = it.key,
                items = it.value,
                isEnable = isEnable,
                onAction = onAction
            )
        }
    }
}

@Composable
private fun DrawRow(
    columnId: ColumnId,
    items: Map<ItemId, ItemStatus>,
    isEnable: Boolean,
    onAction: (ColumnId, ItemId) -> Unit
) {
    Row {
        items.forEach {
            DrawItem(
                columnId = columnId,
                itemId = it.key,
                status = it.value,
                isEnable = isEnable,
                onAction = onAction
            )
        }
    }
}

@Composable
private fun DrawItem(
    columnId: ColumnId,
    itemId: ItemId,
    status: ItemStatus,
    isEnable: Boolean,
    onAction: (ColumnId, ItemId) -> Unit
) {
    Box(
        modifier = Modifier
            .size(90.dp)
            .sideBorder(
                color = Color.LightGray,
                sides = itemId.borderSide
            )
            .clickable(
                enabled = isEnable
            ) {
                onAction(columnId, itemId)
            }
    ) {
        Text(
            text = status.playerName
        )
    }
}

@Preview
@Composable
private fun GameboardScreenPreview() {
    MyApplicationTheme {
        ContentBody(
            data = GameDataFactory.create(),
            onAction = { _, _ -> },
            restartAction = {}
        )
    }
}