package com.app.kekomi.Extras

import android.graphics.Paint
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.drawscope.drawIntoCanvas
import androidx.compose.ui.graphics.nativeCanvas
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlin.math.min
import kotlin.math.roundToInt
import kotlin.math.atan2
import kotlin.math.cos
import kotlin.math.sin



@Preview
@Composable
fun PruebaDonut(){
    val data = listOf(20f, 30f, 50f)
    val colors = listOf(Color.Blue, Color.Green, Color.Red)
    val labels = listOf("Label 1", "Label 2", "Label 3")
    Box(modifier = Modifier.fillMaxSize()) {
        DonutChart(data = data, colors = colors, labels = labels)
    }
}


@Composable
fun DonutChart(
    data: List<Float>,
    colors: List<Color>,
    modifier: Modifier = Modifier,
    labels: List<String>
) {
    var currentAngle = -90f
    var angleAccumulator = 0f
    Canvas(
        modifier = modifier
            .aspectRatio(1f),
        onDraw = {
            val canvasWidth = size.minDimension
            val strokeWidth = canvasWidth * 0.1f
            val center = Offset(size.width / 2, size.height / 2)
            data.forEachIndexed { index, value ->
                val sweepAngle = value * 360f
                drawArc(
                    brush = SolidColor(colors[index]),
                    startAngle = currentAngle + angleAccumulator,
                    sweepAngle = sweepAngle,
                    useCenter = false,
                    style = Stroke(width = strokeWidth),
                    topLeft = Offset(0f, 0f),
                    size = Size(canvasWidth, canvasWidth)
                )
                val textOffset = Offset(
                    center.x + ((canvasWidth / 2 - strokeWidth / 2) * cos(Math.toRadians((currentAngle + angleAccumulator + sweepAngle / 2).toDouble())).toFloat()),
                    center.y + ((canvasWidth / 2 - strokeWidth / 2) * sin(Math.toRadians((currentAngle + angleAccumulator + sweepAngle / 2).toDouble())).toFloat())
                )

                drawIntoCanvas {
                    val label = labels[index]
                    val textPaint = Paint().apply {
                        color = Color.Black.toArgb()
                        textAlign = Paint.Align.CENTER
                        textSize = 32.sp.toPx()
                    }
                    it.nativeCanvas.drawText(label, textOffset.x, textOffset.y, textPaint)
                }
                angleAccumulator += sweepAngle
            }
        }
    )
}

