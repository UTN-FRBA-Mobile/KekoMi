package com.app.kekomi.Extras

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import java.lang.Math.ceil

@Preview
@Composable
fun DonutChart(
    values: List<Float> = listOf(65f, 40f, 25f, 20f),
    colors: List<Color> = listOf(
        Color(android.graphics.Color.parseColor("#008080")),
        Color(android.graphics.Color.parseColor("#195e5e")),
        Color(android.graphics.Color.parseColor("#669494")),
        Color(android.graphics.Color.parseColor("#99b7b7"))
    ),
    legend: List<String> = listOf("Thing1", "Thing2", "Thing3", "Thing4"),//TODO aca se llama a la lista de preferences
) {

    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(4.dp)
    ) {

        CreateArcs(values, colors)

        Spacer(modifier = Modifier.height(32.dp))

        // Calculate the number of rows required based on number of items
        val numberOfRows = ceil(legend.size / 2.0).toInt()

        for (rowIndex in 0 until numberOfRows) {
                Row(
                    horizontalArrangement = Arrangement.spacedBy(16.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    //contentPadding = PaddingValues(horizontal = 16.dp, vertical = 4.dp)
                ) {
                    for (colIndex in 0 until 2) {
                        val legendIndex = (rowIndex * 2) + colIndex
                        if (legendIndex >= legend.size) {
                            // If there are no more legends, break out of the loop
                            break
                        }
                            DisplayLegend(color = colors[legendIndex], legend = legend[legendIndex])
                    }
                }
        }
    }
}

@Composable
fun CreateArcs(values: List<Float>, colors: List<Color>, size: Dp = 100.dp, thickness: Dp = 25.dp){
    // Sum of all the values
    val sumOfValues = values.sum()

    // Calculate each proportion
    val proportions = values.map {
        it * 100 / sumOfValues
    }

    // Convert each proportion to angle
    val sweepAngles = proportions.map {
        360 * it / 100
    }

    Canvas(
        modifier = Modifier
            .size(size = size)
    ) {
        var startAngle = -90f

        for (i in values.indices) {
            drawArc(
                color = colors[i],
                startAngle = startAngle,
                sweepAngle = sweepAngles[i],
                useCenter = false,
                style = Stroke(width = thickness.toPx(), cap = StrokeCap.Butt)
            )
            startAngle += sweepAngles[i]
        }
    }
}

@Composable
fun DisplayLegend(color: Color, legend: String) {

    Row(
        horizontalArrangement = Arrangement.Center,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
            modifier = Modifier
                .size(10.dp)
                .background(color = color, shape = CircleShape)
        )

        Spacer(modifier = Modifier.width(4.dp))

        Text(
            text = legend,
            color = Color.Black
        )
    }
}