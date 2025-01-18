package com.example.tictactoe

import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.update

class Greeting {
    private val platform: Platform = getPlatform()
    private val phrases: List<String> = emptyList()
    private val _state: MutableStateFlow<List<String>> = MutableStateFlow(phrases)
    val state = _state.asStateFlow()

    fun greet(): String {
        return "Hello, ${platform.name}!"
    }

    fun countToTen(): Flow<String> = flow {
        (1..10).forEach {
            val result = "Counter value = $it"
            emit(result)
            delay(1000)
        }
    }

    fun updateValues(value: String) {
        val newValue = phrases + value
        _state.update {
            newValue
        }
    }
}