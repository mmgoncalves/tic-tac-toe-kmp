package com.example.tictactoe.android

import android.widget.Space
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import cafe.adriel.voyager.core.screen.Screen
import com.example.tictactoe.Domain.GameDataFactory

class StartGameScreen : Screen {

    @Composable
    override fun Content() {
        ContentBody()
    }
}

@Composable
private fun ContentBody() {
    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Bottom
    ) {
        Text(
            text = "Start Game",
            modifier = Modifier.fillMaxWidth(),
            textAlign = TextAlign.Center
        )
//        Spacer(modifier = Modifier.weight(1f))
        Button(
            modifier = Modifier
                .fillMaxWidth()
                .padding(all = 40.dp),
            onClick = {},
            content = {
                Text("Começar")
            }
        )
    }
}

@Preview
@Composable
private fun StartGamePreview() {
    MyApplicationTheme {
        ContentBody()
    }
}