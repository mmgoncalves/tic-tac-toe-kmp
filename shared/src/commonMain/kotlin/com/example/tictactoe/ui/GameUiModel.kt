package com.example.tictactoe.ui

import com.example.tictactoe.Domain.ColumnId
import com.example.tictactoe.Domain.GameData
import com.example.tictactoe.Domain.GameDataFactory
import com.example.tictactoe.Domain.GameResult
import com.example.tictactoe.Domain.GameStatus
import com.example.tictactoe.Domain.GameUiData
import com.example.tictactoe.Domain.ItemId
import com.example.tictactoe.Domain.ItemStatus
import com.example.tictactoe.Domain.PlayerData
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

public class GameUiModel(
    private val data: GameData = GameDataFactory.create(),
    private val player1: PlayerData = PlayerData(ItemStatus.X),
    private val player2: PlayerData = PlayerData(ItemStatus.O)
) {

    private val _state = MutableStateFlow(data)
    val state = _state.asStateFlow()

    private var currentPlayer = player1

    init {
        updateHeaderMessage()
    }

    fun updateItem(columnId: ColumnId, itemId: ItemId) {
        val status = state.value.columns.getValue(columnId).getValue(itemId)
        if (status != ItemStatus.EMPTY) {
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

        toggleCurrentUser()
        _state.update {
            state.value.copy(
                columns = updatedData
            )
        }

        updateGameResult()
    }

    private fun toggleCurrentUser() {
        currentPlayer = if (currentPlayer == player1) player2 else { player1 }
        _state.update { state.value.copy(currentPlayer = currentPlayer) }
    }

    private fun updateGameResult() {
        if (checkPlayerWinner(player1)) {
            _state.update { state.value.copy(gameStatus = GameStatus(result = GameResult.Winner, winner = player1)) }
        } else if (checkPlayerWinner(player2)) {
            _state.update { state.value.copy(gameStatus = GameStatus(result = GameResult.Winner, winner = player2)) }
        } else if (checkGameFinish()) {
            _state.update { state.value.copy(gameStatus = GameStatus(result = GameResult.Tie)) }
        } else {
            _state.update { state.value.copy(gameStatus = GameStatus(result = GameResult.Playing)) }
        }
        updateHeaderMessage()
    }

    private fun checkGameFinish(): Boolean {
        val result = state.value.columns.values.none { columns ->
            columns.values.none { it == ItemStatus.EMPTY }
        }
        return result.not()
    }

    private fun checkPlayerWinner(
        player: PlayerData
    ): Boolean {
        val items = state.value.columns
        val a1 = items.getValue(ColumnId.A).getValue(ItemId.A1)
        val a2 = items.getValue(ColumnId.A).getValue(ItemId.A2)
        val a3 = items.getValue(ColumnId.A).getValue(ItemId.A3)
        val b1 = items.getValue(ColumnId.B).getValue(ItemId.B1)
        val b2 = items.getValue(ColumnId.B).getValue(ItemId.B2)
        val b3 = items.getValue(ColumnId.B).getValue(ItemId.B3)
        val c1 = items.getValue(ColumnId.C).getValue(ItemId.C1)
        val c2 = items.getValue(ColumnId.C).getValue(ItemId.C2)
        val c3 = items.getValue(ColumnId.C).getValue(ItemId.C3)

        if (a1 == a2 && a1 == a3) {
            return  player.status == a1
        }

        if (b1 == b2 && b1 == b3) {
            return  player.status == b1
        }

        if (c1 == c2 && c1 == c3) {
            return  player.status == c1
        }

        if (a1 == b1 && a1 == c1) {
            return  player.status == a1
        }

        if (a2 == b2 && a2 == c2) {
            return  player.status == a2
        }

        if (a3 == b3 && a3 == c3) {
            return  player.status == a3
        }

        if (a1 == b2 && a1 == c3) {
            return  player.status == a1
        }

        if (a3 == b2 && a3 == c1) {
            return  player.status == a3
        }

        return false
    }

    private fun updateHeaderMessage() {
        val message = when (state.value.gameStatus.result) {
            GameResult.Playing -> "É a vez do jogador: ${state.value.currentPlayer.status.playerName}"
            GameResult.Tie -> "O Jogo terminou empatado"
            GameResult.Winner -> "Jogo finalizado, vencedor: ${state.value.gameStatus.winner?.status?.playerName}"
        }
        _state.update {
            state.value.copy(headerMessage = message)
        }
    }
}