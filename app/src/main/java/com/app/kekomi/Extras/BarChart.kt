package com.app.kekomi.Extras

import android.graphics.Paint
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.layout.Arrangement.Top
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Alignment.Companion.BottomCenter
import androidx.compose.ui.Alignment.Companion.CenterHorizontally
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.PathEffect
import androidx.compose.ui.graphics.nativeCanvas
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlin.math.round

private val defaultMaxHeight = 200.dp

@Preview
@Composable
fun TestBarChart(){
    BarChartByTimePeriod("Week")
}

//TODO LOGICA DE ESTO CUANDO VEAMOS LA DB
@Composable
fun BarChartByTimePeriod( selectedTimePeriod: String){
    when (selectedTimePeriod) {
        "Week" -> {
            // agarro data los ultimos 7 dias, una barra por dia
            BarChart(values = listOf(65f, 40f, 25f, 20f,10f,40f,30f))
        }
        "Month" -> {
            // agarro los ultimos 28 dias (a fines practicos), una barra por semana
            BarChart(values = listOf(65f, 40f, 25f, 20f))
        }
        "6Months" -> {
            // agarro los ultimos 6 meses, una barra por mes
            BarChart(values = listOf(65f, 40f, 25f, 20f,10f,40f))
        }
        "Year" -> {
            // agarro los ultimos 12 meses, una barra por mes
            //barChart2(graphBarData = listOf(65f, 40f, 25f, 20f,10f,40f,30f,50f,80f,90f,40f,27f))
            barChart2()
        }
    }
}



@Composable
fun BarChart(
    modifier: Modifier = Modifier,
    values: List<Float>,
    maxHeight: Dp = defaultMaxHeight
) {

    assert(values.isNotEmpty()) { "Input values are empty" }

    val borderColor = Color.Gray
    val density = LocalDensity.current
    val strokeWidth = with(density) { 5.dp.toPx() }

    Row(
        modifier = modifier.then(
            Modifier
                .fillMaxWidth()
                .height(maxHeight)
                .drawBehind {
                    // draw X-Axis
                    drawLine(
                        color = borderColor,
                        start = Offset(0f, size.height),
                        end = Offset(size.width, size.height),
                        strokeWidth = strokeWidth
                    )
                    // draw Y-Axis
                    drawLine(
                        color = borderColor,
                        start = Offset(0f, 0f),
                        end = Offset(0f, size.height),
                        strokeWidth = strokeWidth
                    )
                }
        ),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.Bottom
    ) {
        values.forEach { item ->
            Bar(
                value = item,
                color = Color(android.graphics.Color.parseColor("#006c6c")),
                maxHeight = maxHeight,
            )

        }
    }

}

@Composable
private fun RowScope.Bar(
    value: Float,
    color: Color,
    maxHeight: Dp
) {

    val itemHeight = remember(value) { value * maxHeight.value / 100 }
   
    Spacer(
        modifier = Modifier
            .padding(horizontal = 5.dp)
            .height(itemHeight.dp)
            .weight(1f)
            .background(color)
    )

}