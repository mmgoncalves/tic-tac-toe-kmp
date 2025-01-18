package com.example.tictactoe.android.helper

import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.example.tictactoe.ui.BorderSide

fun Modifier.sideBorder(
    color: Color,
    thickness: Dp = 1.dp,
    sides: Set<BorderSide?>
): Modifier = this.drawBehind {
    val strokeWidth = thickness.toPx()
    val halfStroke = strokeWidth / 2

    if (BorderSide.Left in sides) {
        drawLine(
            color = color,
            start = Offset(halfStroke, 0f),
            end = Offset(halfStroke, size.height),
            strokeWidth = strokeWidth
        )
    }

    if (BorderSide.Top in sides) {
        drawLine(
            color = color,
            start = Offset(0f, halfStroke),
            end = Offset(size.width, halfStroke),
            strokeWidth = strokeWidth
        )
    }

    if (BorderSide.Right in sides) {
        drawLine(
            color = color,
            start = Offset(size.width - halfStroke, 0f),
            end = Offset(size.width - halfStroke, size.height),
            strokeWidth = strokeWidth
        )
    }

    if (BorderSide.Bottom in sides) {
        drawLine(
            color = color,
            start = Offset(0f, size.height - halfStroke),
            end = Offset(size.width, size.height - halfStroke),
            strokeWidth = strokeWidth
        )
    }
}