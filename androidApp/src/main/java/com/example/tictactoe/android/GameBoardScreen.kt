package com.example.tictactoe.android

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.IntSize
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import cafe.adriel.voyager.core.model.ScreenModel
import cafe.adriel.voyager.core.model.rememberScreenModel
import cafe.adriel.voyager.core.screen.Screen
import com.example.tictactoe.domain.ColumnId
import com.example.tictactoe.domain.GameData
import com.example.tictactoe.domain.GameDataFactory
import com.example.tictactoe.domain.GameResult
import com.example.tictactoe.domain.ItemId
import com.example.tictactoe.domain.ItemStatus
import com.example.tictactoe.android.helper.sideBorder
import com.example.tictactoe.ui.BorderSide
import com.example.tictactoe.ui.GameUiModel

class GameBoardModel : ScreenModel {}

data class GameBoardScreen(
    private val uiModel: GameBoardModel,
    private val gameUiModel: GameUiModel = GameUiModel()
): Screen {

    @Composable
    override fun Content() {
        val screenModel = rememberScreenModel { uiModel }
        val model by remember { mutableStateOf(gameUiModel) }
        val state by model.state.collectAsState()

        ContentBody(
            data = state,
            onAction = { columnId, itemId ->
                model.updateItem(columnId, itemId)
            },
            restartAction = {
                model.restartGame()
            }
        )
    }
}

@Composable
private fun ContentBody(
    data: GameData,
    onAction: (ColumnId, ItemId) -> Unit,
    restartAction: () -> Unit
) {
    var size by remember { mutableStateOf(IntSize.Zero) }

    MyApplicationTheme {
        Box(
            contentAlignment = Alignment.Center,
            modifier = Modifier
                .background(color = Color.White)
                .fillMaxSize()
                .onSizeChanged { size = it }
        ) {
            DrawColumn(data, size, onAction, restartAction)
        }
    }
}

@Composable
private fun DrawColumn(
    data: GameData,
    size: IntSize,
    onAction: (ColumnId, ItemId) -> Unit,
    restartAction: () -> Unit
) {
    val result = data.gameStatus.result
    val headerText = if (result == GameResult.Playing) {
        result.getMessage() + data.currentPlayer.status.playerName
    } else {
        if (data.gameStatus.winner != null) {
            result.getMessage() + data.gameStatus.winner!!.status.playerName
        } else {
            result.getMessage()
        }
    }
    Column {
        Text(
            text = headerText ?: "VAZIO",
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 10.dp)
                .padding(horizontal = 20.dp)
                .sideBorder(
                    color = Color.DarkGray,
                    thickness = 0.3f.dp,
                    sides = setOf(BorderSide.Bottom)
                )
                .padding(bottom = 10.dp)
            ,
            style = TextStyle(
                fontSize = 18.sp,
                color = Color.DarkGray,
                fontWeight = FontWeight.SemiBold,
                textAlign = TextAlign.Center,
            )
        )
        data.columns.forEach {
            DrawRow(
                columnId = it.key,
                items = it.value,
                size = size,
                onAction = onAction
            )
        }

        if (data.showRestartButton) {
            Button(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(all = 20.dp),
                colors = ButtonDefaults.buttonColors(containerColor = Color.Cyan),
                onClick = restartAction,
                content = {
                    Text(
                        text = "Reiniciar partida",
                        fontWeight = FontWeight.Bold,
                        color = Color.DarkGray
                    )
                }
            )
        }
    }
}

@Composable
private fun DrawRow(
    columnId: ColumnId,
    items: Map<ItemId, ItemStatus>,
    size: IntSize,
    onAction: (ColumnId, ItemId) -> Unit
) {
    Row {
        items.forEach {
            DrawItem(
                columnId = columnId,
                itemId = it.key,
                status = it.value,
                size = size,
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
    size: IntSize,
    onAction: (ColumnId, ItemId) -> Unit
) {
    var width by remember { mutableStateOf(Dp.Infinity) }
    var height by remember { mutableStateOf(Dp.Infinity) }
    with(LocalDensity.current) {
        width = size.width.toDp().div(3)
        height = size.height.toDp().div(3.8f)
    }
    Box(
        contentAlignment = Alignment.Center,
        modifier = Modifier
            .size(width, height)
            .sideBorder(
                color = Color.LightGray,
                sides = itemId.borderSide
            )
            .clickable {
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
private fun DefaultPreview() {
    MyApplicationTheme {
        ContentBody(
            data = GameDataFactory.create(),
            onAction = { _, _ -> },
            restartAction = {}
        )
    }
}