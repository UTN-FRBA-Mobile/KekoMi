package com.app.kekomi.Extras

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.app.kekomi.storage.FoodRepository
import com.app.kekomi.storage.userPreferences
import com.app.kekomi.ui.theme.principalColor
import java.lang.Math.ceil

@Composable
fun getMetrics(): List<String> {
    val context = LocalContext.current
    //scope
    val scope = rememberCoroutineScope()
    // datastore
    val dataStore = userPreferences(context)

    val metrics: List<String> = getCheckedItems(dataStore = dataStore)

    return if(metrics.isEmpty()){
        listOf("EMPTY", "EMPTY")
    }else{
        metrics
    }
}

@Composable
fun getCheckedItems(dataStore: userPreferences): List<String> {
    val items = dataStore.getItems()
    var states = dataStore.getStates.map { item -> item.collectAsState(initial = "").value!!}
    states = states.map{ item -> item.toString().toBoolean() }


    val checkedItems = items.take(items.size).filterIndexed() { index, _ -> states[index] }

    val updatedItems = checkedItems.filterNot { item -> item == "Calories" }
    return updatedItems

}

@Composable
fun getValues(): List<Float> {
    val context = LocalContext.current
    val repository = FoodRepository(context)

    val metrics: List<String> = getMetrics()

    var floatValues: List<Float> = listOf()

    for (metric in metrics) {

        val stats = repository.getStatsFrom(DateSelected.pickedDate)

        val value: Float = when (metric) {
            "Calories" -> stats?.calories?.toFloat() ?: 0f
            "Proteins" -> stats?.protein?.toFloat() ?: 0f
            "Fats" -> stats?.fats?.toFloat() ?: 0f
            "Sodium" -> stats?.sodium?.toFloat() ?: 0f
            "Sugar" -> stats?.sugar?.toFloat() ?: 0f
            else -> 0f
        }

        floatValues += value.toFloat()

    }
    return floatValues
}

@Composable
fun getColors(values: List<Float>): List<Color>{

    var colores:List<Color> = if(areAllZero(values)){
        listOf(Color.Gray, Color.Gray, Color.Gray, Color.Gray)
    }else{
        listOf(
            principalColor,
            Color(android.graphics.Color.parseColor("#195e5e")),
            Color(android.graphics.Color.parseColor("#669494")),
            Color(android.graphics.Color.parseColor("#99b7b7"))
        )
    }
    return colores
}
@Preview
@Composable
fun DonutChart(
    values: List<Float> = getValues(),
    colors: List<Color> = getColors(values),
    legend: List<String> = getMetrics()
){
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
                        val sumOfValues = values.sum()
                        val proportions = values.map {
                            it * 100 / sumOfValues
                        }
                        if(legend[0]!="EMPTY") {
                            DisplayLegend(
                                color = colors[legendIndex],
                                legend = legend[legendIndex],
                                value = proportions[legendIndex]
                            )
                        }
                    }
                }
        }
    }
}

fun areAllZero(list: List<Float>): Boolean {
    return list.all { it == 0f }
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
fun DisplayLegend(color: Color, legend: String, value:Float) {

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
            text = "${legend} \n ${value.toInt()}%",
            color = Color.Black
        )
    }
}