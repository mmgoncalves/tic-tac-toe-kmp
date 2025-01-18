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
import com.example.tictactoe.Domain.ColumnId
import com.example.tictactoe.Domain.GameData
import com.example.tictactoe.Domain.GameDataFactory
import com.example.tictactoe.Domain.GameResult
import com.example.tictactoe.Domain.ItemId
import com.example.tictactoe.Domain.ItemStatus
import com.example.tictactoe.android.helper.sideBorder
import com.example.tictactoe.ui.BorderSide
import com.example.tictactoe.ui.GameUiModel

@Composable
fun ContentView(model: GameUiModel = GameUiModel()) {
    val uiModel by remember { mutableStateOf(model) }
    val state by uiModel.state.collectAsState()
    ContentBody(
        data = state,
        onAction = { columnId, itemId ->
            uiModel.updateItem(columnId, itemId)
        }
    )
}

@Composable
private fun ContentBody(
    data: GameData,
    onAction: (ColumnId, ItemId) -> Unit
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
            DrawColumn(data, size, onAction)
        }
    }
}

@Composable
private fun DrawColumn(
    data: GameData,
    size: IntSize,
    onAction: (ColumnId, ItemId) -> Unit
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
//    val headerText = data.gameStatus.result.text
//        .takeIf {
//            data.gameStatus.result != GameResult.Playing
//        } ?: "É a vez do jogador ${data.currentPlayer.status.playerName}"
    Column {
        Text(
            text = headerText ?: "VAZIO",
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 20.dp)
                .padding(horizontal = 20.dp)
                .sideBorder(
                    color = Color.Gray,
                    thickness = 0.3f.dp,
                    sides = setOf(BorderSide.Bottom)
                ),
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
fun DefaultPreview() {
    MyApplicationTheme {
        ContentBody(
            data = GameDataFactory.create(),
            onAction = { _, _ -> }
        )
    }
}