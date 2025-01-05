package com.example.tictactoe.Domain

public data class GameData(
    val columns: List<Row> = emptyList()
)

public data class Row(
    val rows: List<Item> = emptyList()
)

public data class Item(
    val status: ItemStatus = ItemStatus.EMPTY
)

public enum class ItemStatus {
    EMPTY,
    X,
    O
}

public object GameDataFactory {
    fun create(): GameData {
        return GameData(
            List(3) {
                Row(
                    List(3) {
                        Item()
                    }
                )
            }
        )
    }
}