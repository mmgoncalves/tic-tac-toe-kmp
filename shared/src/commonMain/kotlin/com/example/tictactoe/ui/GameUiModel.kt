package com.example.tictactoe.ui

import com.example.tictactoe.Domain.GameData
import com.example.tictactoe.Domain.GameDataFactory
import com.example.tictactoe.Domain.ItemStatus

public class GameUiModel(var data: GameData = GameDataFactory.create()) {
    fun updateData(data: GameData) {
        this.data = data
    }

}