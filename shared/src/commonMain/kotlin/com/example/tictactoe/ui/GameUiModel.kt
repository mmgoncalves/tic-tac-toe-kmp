package com.example.tictactoe.ui

import com.example.tictactoe.domain.ColumnId
import com.example.tictactoe.domain.GameData
import com.example.tictactoe.domain.GameDataFactory
import com.example.tictactoe.domain.GameResult
import com.example.tictactoe.domain.GameStatus
import com.example.tictactoe.domain.ItemId
import com.example.tictactoe.domain.ItemStatus
import com.example.tictactoe.domain.PlayerData
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

public class GameUiModel(
    private val player1: PlayerData,
    private val player2: PlayerData
) {
    private var currentPlayer = player1
    private val data: GameData = GameDataFactory.create(currentPlayer)
    private val _state = MutableStateFlow(data)
    val state = _state.asStateFlow()

    init {
        updateHeaderMessage()
    }

    fun restartGame() {
        _state.update { data }
        updateHeaderMessage()
    }

    fun updateItem(columnId: ColumnId, itemId: ItemId) {
        val status = state.value.columns.getValue(columnId).getValue(itemId)
        if (status != ItemStatus.EMPTY) {
            return
        }

        if (state.value.gameStatus.result != GameResult.Playing) {
            return
        }

        val updatedColumn = state.value.columns[columnId]?.toMutableMap()?.apply {
            this[itemId] = currentPlayer.status
        }

        val updatedData = state.value.columns.toMutableMap().apply {
            if (updatedColumn != null) {
                this[columnId] = updatedColumn
            }
        }

        _state.update {
            state.value.copy(
                columns = updatedData
            )
        }

        updateGameResult()
        toggleCurrentUser()
    }

    private fun toggleCurrentUser() {
        if (state.value.gameStatus.result != GameResult.Playing) {
            return
        }
        currentPlayer = if (currentPlayer == player1) player2 else { player1 }
        _state.update { state.value.copy(currentPlayer = currentPlayer) }
    }

    private fun updateGameResult() {
        if (checkPlayerWinner(player1)) {
            _state.update { state.value.copy(
                gameStatus = GameStatus(result = GameResult.Winner, winner = player1),
                showRestartButton = true
            ) }
        } else if (checkPlayerWinner(player2)) {
            _state.update { state.value.copy(
                gameStatus = GameStatus(result = GameResult.Winner, winner = player2),
                showRestartButton = true
            ) }
        } else if (checkGameFinish()) {
            _state.update { state.value.copy(
                gameStatus = GameStatus(result = GameResult.Tie),
                showRestartButton = true
            ) }
        } else {
            _state.update { state.value.copy(
                gameStatus = GameStatus(result = GameResult.Playing),
                showRestartButton = false
            ) }
        }
        updateHeaderMessage()
    }

    private fun checkGameFinish(): Boolean {
        val result = state.value.columns.values.fold(0) { acc, value ->
            val isEmpty = value.values.none { it == ItemStatus.EMPTY }
            acc + if (isEmpty) 0 else 1
        }

        return result == 0
    }

    private fun checkPlayerWinner(player: PlayerData): Boolean {
        val items = state.value.columns

        val positions = mapOf(
            "A1" to items.getValue(ColumnId.A).getValue(ItemId.A1),
            "A2" to items.getValue(ColumnId.A).getValue(ItemId.A2),
            "A3" to items.getValue(ColumnId.A).getValue(ItemId.A3),
            "B1" to items.getValue(ColumnId.B).getValue(ItemId.B1),
            "B2" to items.getValue(ColumnId.B).getValue(ItemId.B2),
            "B3" to items.getValue(ColumnId.B).getValue(ItemId.B3),
            "C1" to items.getValue(ColumnId.C).getValue(ItemId.C1),
            "C2" to items.getValue(ColumnId.C).getValue(ItemId.C2),
            "C3" to items.getValue(ColumnId.C).getValue(ItemId.C3),
        )

        val winningCombinations = listOf(
            listOf("A1", "A2", "A3"),
            listOf("B1", "B2", "B3"),
            listOf("C1", "C2", "C3"),
            listOf("A1", "B1", "C1"),
            listOf("A2", "B2", "C2"),
            listOf("A3", "B3", "C3"),
            listOf("A1", "B2", "C3"),
            listOf("A3", "B2", "C1")
        )

        return winningCombinations.any { combination ->
            val (first, second, third) = combination
            val firstStatus = positions[first]
            val secondStatus = positions[second]
            val thirdStatus = positions[third]

            firstStatus != ItemStatus.EMPTY &&
                    firstStatus == secondStatus &&
                    firstStatus == thirdStatus &&
                    player.status == firstStatus
        }
    }

    private fun updateHeaderMessage() {
        _state.update {
            state.value.copy(headerMessage = state.value.gameStatus.result.getMessage())
        }
    }
}