package com.example.tictactoe.domain

import com.example.tictactoe.ui.BorderSide

typealias GameUiData = Map<ColumnId, Map<ItemId, ItemStatus>>
data class GameData(
    val columns: GameUiData,
    val currentPlayer: PlayerData,
    val gameStatus: GameStatus,
    val headerMessage: String,
    val showRestartButton: Boolean
)

data class GameStatus(
    val result: GameResult,
    val winner: PlayerData? = null
)

enum class GameResult(val text: String) {
    Playing("É a vez do jogador: ") {
        override fun getMessage(): String = text
    },
    Tie("O Jogo terminou empatado") {
        override fun getMessage(): String = text
    },
    Winner("Jogo finalizado, vencedor: ") {
        override fun getMessage(): String = text
    };

    abstract fun getMessage(): String
}

enum class ItemStatus {
    EMPTY { override val playerName: String = "" },
    X { override val playerName: String = "X" },
    O { override val playerName: String = "O" };

    abstract val playerName: String
}

enum class ColumnId {
    A, B, C
}

enum class ItemId {
    A1 { override val borderSide: Set<BorderSide?> = setOf(BorderSide.Right, BorderSide.Bottom)
},
    A2 { override val borderSide: Set<BorderSide?> = setOf(BorderSide.Bottom)
},
    A3 { override val borderSide: Set<BorderSide?> = setOf(BorderSide.Left, BorderSide.Bottom)
},
    B1 { override val borderSide: Set<BorderSide?> = setOf(BorderSide.Right, BorderSide.Bottom)
},
    B2 { override val borderSide: Set<BorderSide?> = setOf(BorderSide.Bottom)
},
    B3 { override val borderSide: Set<BorderSide?> = setOf(BorderSide.Left, BorderSide.Bottom)
},
    C1 { override val borderSide: Set<BorderSide?> = setOf(BorderSide.Right)
},
    C2 { override val borderSide: Set<BorderSide?> = setOf(null)
},
    C3 { override val borderSide: Set<BorderSide?> = setOf(BorderSide.Left)
};

    abstract val borderSide: Set<BorderSide?>
}

object GameDataFactory {
    fun create(
        currentPlayer: PlayerData
    ): GameData {
        return GameData(
            columns = mapOf(
                ColumnId.A to mapOf(
                    ItemId.A1 to ItemStatus.EMPTY,
                    ItemId.A2 to ItemStatus.EMPTY,
                    ItemId.A3 to ItemStatus.EMPTY,
                ),

                ColumnId.B to mapOf(
                    ItemId.B1 to ItemStatus.EMPTY,
                    ItemId.B2 to ItemStatus.EMPTY,
                    ItemId.B3 to ItemStatus.EMPTY,
                ),

                ColumnId.C to mapOf(
                    ItemId.C1 to ItemStatus.EMPTY,
                    ItemId.C2 to ItemStatus.EMPTY,
                    ItemId.C3 to ItemStatus.EMPTY,
                )
            ),
            currentPlayer = currentPlayer,
            gameStatus = GameStatus(GameResult.Playing),
            headerMessage = "",
            showRestartButton = false
        )
    }
}