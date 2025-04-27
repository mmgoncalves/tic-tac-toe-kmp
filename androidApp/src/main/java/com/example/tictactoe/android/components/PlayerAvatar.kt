package com.example.tictactoe.android.components

import androidx.annotation.DrawableRes
import androidx.compose.foundation.Image
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource

@Composable
fun PlayerAvatar(@DrawableRes id: Int, modifier: Modifier = Modifier) {
    Image(
        modifier = modifier,
        painter = painterResource(id = id),
        contentDescription = null,
        alignment = Alignment.Center,
        contentScale = ContentScale.Fit
    )
}