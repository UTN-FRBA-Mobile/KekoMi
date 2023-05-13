package com.app.kekomi.Extras

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

private val defaultMaxHeight = 200.dp

@Preview
@Composable
fun TestBarChart(){
    BarChartByTimePeriod("week")
}

//TODO LOGICA DE ESTO CUANDO VEAMOS LA DB
@Composable
fun BarChartByTimePeriod( timePeriod: String){
    when (timePeriod) {
        "week" -> {
            // agarro data los ultimos 7 dias, una barra por dia
            BarChart(values = listOf(65f, 40f, 25f, 20f,10f,40f,30f))
        }
        "month" -> {
            // agarro los ultimos 28 dias (a fines practicos), una barra por semana
            BarChart(values = listOf(65f, 40f, 25f, 20f))
        }
        "sixMonths" -> {
            // agarro los ultimos 6 meses, una barra por mes
            BarChart(values = listOf(65f, 40f, 25f, 20f,10f,40f))
        }
        "year" -> {
            // agarro los ultimos 12 meses, una barra por mes
            BarChart(values = listOf(65f, 40f, 25f, 20f,10f,40f,30f,50f,80f,90f,40f,27f))
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

    val borderColor = Color(android.graphics.Color.parseColor("#008080"))
    val density = LocalDensity.current
    val strokeWidth = with(density) { 1.dp.toPx() }

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
                color = Color(android.graphics.Color.parseColor("#195e5e")),
                maxHeight = maxHeight
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