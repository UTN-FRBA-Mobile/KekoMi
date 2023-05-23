package com.app.kekomi.Extras

import android.graphics.Paint
import android.util.Log
import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.LinearOutSlowInEasing
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.layout.Arrangement.Top
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Alignment.Companion.BottomCenter
import androidx.compose.ui.Alignment.Companion.CenterHorizontally
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Outline
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.PathEffect
import androidx.compose.ui.graphics.drawOutline
import androidx.compose.ui.graphics.drawscope.drawIntoCanvas
import androidx.compose.ui.graphics.nativeCanvas
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.graphics.ColorUtils
import com.app.kekomi.storage.FoodRepository
import kotlinx.coroutines.delay
import androidx.compose.ui.platform.LocalContext
import com.app.kekomi.Views.ProgressBarWithText
import com.app.kekomi.Views.getGoals
import com.app.kekomi.entities.Stats
import com.app.kekomi.storage.userPreferences

import kotlin.math.round


//TODO LOGICA DE ESTO CUANDO VEAMOS LA DB
@Composable
fun BarChartByTimePeriod( selectedTimePeriod: String){
    val context = LocalContext.current
    val repository = FoodRepository(context)

    when (selectedTimePeriod) {
        "Week" -> {
            // agarro data los ultimos 7 dias, una barra por dia
            var weekValues = repository.getWeekStats().reversed()

            barChartCall(valuesList = weekValues)

        }
        "Month" -> {
            // agarro los ultimos 28 dias (a fines practicos), una barra por semana

            var monthValues = repository.getMonthStats().reversed()

            barChartCall(valuesList = monthValues)
        }
        "6Months" -> {
            // agarro los ultimos 6 meses, una barra por mes

            /*
            var sixMonthsValues = repository.get6MonthStats().reversed()

            barChartCall(valuesList = sixMonthsValues)*/
        }
        "Year" -> {
            // agarro los ultimos 12 meses, una barra por mes

            /*
            var yearValues = repository.getYearStats().reversed()

            barChartCall(valuesList = yearValues)*/
        }
    }
}



@Composable
fun barChartCall(valuesList: List<Stats>){

    val context = LocalContext.current
    // scope
    val scope = rememberCoroutineScope()
    // datastore
    val dataStore = userPreferences(context)

    val metrics = com.app.kekomi.Views.getCheckedItems(dataStore)

    for (metric in metrics) {

        val valuesByMetric: List<Float> = getValuesByMetric(metric = metric, statsList = valuesList)

        val dataPair: Map<Any, Float> = getDataPair(valuesList = valuesByMetric)

        var showChart1 by remember {
            mutableStateOf(false)
        }

        Chart(
            //TODO DE LOS STATS HAY QUE SACAR LA LISTA DE CALORIES, SODIUM, ETC
            data = dataPair,
            label="${metric}",
            isExpanded = showChart1
        ) {
            showChart1 = !showChart1
        }
    }
}

@Composable
fun getDataPair(valuesList: List<Float>): Map<Any, Float> {

    val listCount = valuesList.count()
    var dataPair : Map<Any, Float> = mapOf()

    when(listCount){
        7 ->{
            dataPair = mapOf(
                Pair("Mon", valuesList[0]),
                Pair("Tue", valuesList[1]),
                Pair("Wed", valuesList[2]),
                Pair("Thu", valuesList[3]),
                Pair("Fri", valuesList[4]),
                Pair("Sat", valuesList[5]),
                Pair("Sun", valuesList[6]))
        }
        4 ->{
            dataPair = mapOf(
                Pair("Week 1", valuesList[0]),
                Pair("Week 2", valuesList[1]),
                Pair("Week 3", valuesList[2]),
                Pair("Week 4", valuesList[3]))
        }
        6 ->{
            dataPair = mapOf(
                Pair("Jan", valuesList[0]),
                Pair("Feb", valuesList[1]),
                Pair("Mar", valuesList[2]),
                Pair("Apr", valuesList[3]),
                Pair("May", valuesList[4]),
                Pair("Jun", valuesList[5]))
        }
        12 ->{
            dataPair = mapOf(
                Pair("Jan", valuesList[0]),
                Pair("Feb", valuesList[1]),
                Pair("Mar", valuesList[2]),
                Pair("Apr", valuesList[3]),
                Pair("May", valuesList[4]),
                Pair("Jun", valuesList[5]),
                Pair("Jul", valuesList[6]),
                Pair("Aug", valuesList[7]),
                Pair("Sep", valuesList[8]),
                Pair("Oct", valuesList[9]),
                Pair("Nov", valuesList[10]),
                Pair("Dec", valuesList[11]))
        }
    }
    return dataPair
}

@Composable
fun getValuesByMetric(metric: String,statsList: List<Stats>): List<Float> {

    var valuesByMetric: List<Float> = listOf()

    for(stat in statsList){
        when(metric){
            "Calories" -> {
                valuesByMetric += stat.calories.toFloat()
            }
            "Proteins" -> {
                valuesByMetric += stat.protein.toFloat()
            }
            "Fats" -> {
                valuesByMetric += stat.fats.toFloat()
            }
            "Sodium" -> {
                valuesByMetric += stat.sodium.toFloat()
            }
            "Sugar" -> {
                valuesByMetric += stat.sugar.toFloat()
            }
        }
    }

    return valuesByMetric
}

@Composable
fun Chart(
    label: String,
    data: Map<Any, Float>,
    barCornersRadius: Float = 10f,
    barColor: Color = Color(android.graphics.Color.parseColor("#006c6c")),
    barWidth: Float = 50f,
    height: Dp= 250.dp,
    labelOffset: Float = 60f,
    labelColor: Color = Color.Black,
    backgroundColor: Color = Color.White,
    topStartRadius: Dp = 0.dp,
    topEndRadius: Dp = 0.dp,
    bottomStartRadius: Dp = 0.dp,
    bottomEndRadius: Dp = 0.dp,
    isExpanded: Boolean = true,
    closeIcon: ImageVector = Icons.Default.KeyboardArrowUp,
    onCloseListener: () -> Unit
) {


    val shape = RoundedCornerShape(
        topStart = topStartRadius,
        topEnd = topEndRadius,
        bottomEnd = bottomEndRadius,
        bottomStart = bottomStartRadius
    )


    var screenSize by remember {
        mutableStateOf(Size.Zero)
    }

    var chosenBar by remember {
        mutableStateOf(-1)
    }
    var chosenBarKey by remember {
        mutableStateOf("")
    }

    val cardHeight by animateDpAsState(
        targetValue = if (isExpanded) height else 50.dp,
        animationSpec = tween(
            1000,
            easing = LinearOutSlowInEasing
        )
    )

    val rotate by animateFloatAsState(
        targetValue = if (isExpanded) 0f else 180f,
        animationSpec = tween(
            700,
            easing = LinearOutSlowInEasing
        )
    )

    LaunchedEffect(chosenBar) {
        delay(3000)
        chosenBarKey = ""
    }

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(cardHeight)
            .shadow(2.dp, shape = shape)
            .clip(shape = shape)
            .background(
                color = backgroundColor,
                shape = shape
            )
            .animateContentSize()
    ) {

        IconButton(onClick = onCloseListener) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(
                    imageVector = closeIcon,
                    contentDescription = "Close chart",
                    modifier = Modifier.rotate(rotate)
                )
                Text(
                    text = "${label}",
                    style = MaterialTheme.typography.button,
                    modifier = Modifier.padding(start = 4.dp)
                )
            }
        }
        Canvas(modifier = Modifier
            .fillMaxSize()
            .alpha(if (cardHeight < height) (cardHeight - 90.dp) / height else 1f)
            .padding(
                top = 65.dp,
                bottom = 20.dp,
                start = 30.dp,
                end = 30.dp
            )
            .pointerInput(Unit) {
                this.detectTapGestures(onPress = {
                    chosenBar = detectPosition(
                        screenSize = screenSize,
                        offset = it,
                        listSize = data.size,
                        itemWidth = barWidth
                    )
                    if (chosenBar >= 0) {
                        chosenBarKey = data.toList()[chosenBar].first.toString()
                    }
                })
            },
            onDraw = {
                screenSize = size
                val spaceBetweenBars =
                    (size.width - (data.size * barWidth)) / (data.size - 1)
                val maxBarHeight = data.values.maxOf { it }
                val barScale = size.height / maxBarHeight
                val paint = Paint().apply {
                    this.color = labelColor.toArgb()
                    textAlign = Paint.Align.CENTER
                    textSize = 40f
                }

                var spaceStep = 0f

                for (item in data) {
                    val topLeft = Offset(
                        x = spaceStep,
                        y = size.height - item.value * barScale - labelOffset
                    )
                    //--------------------(draw bars)--------------------//
                    drawRoundRect(
                        color = barColor,
                        topLeft = topLeft,
                        size = Size(
                            width = barWidth,
                            height =size.height - topLeft.y - labelOffset
                        ),
                        cornerRadius = CornerRadius(barCornersRadius, barCornersRadius)
                    )
                    //--------------------(showing the x axis labels)--------------------//
                    drawContext.canvas.nativeCanvas.drawText(
                        item.key.toString(),
                        spaceStep + barWidth / 2,
                        size.height,
                        paint
                    )
                    //--------------------(showing the bar label)--------------------//
                    if (chosenBarKey == item.key.toString()) {
                        val localLabelColor = Color(
                            ColorUtils.blendARGB(
                                Color.White.toArgb(), barColor.toArgb(), 0.4f
                            )
                        )
                        drawRoundRect(
                            color = localLabelColor,
                            topLeft = Offset(x = topLeft.x - 40f, y = topLeft.y - 100),
                            size = Size(140f, 80f),
                            cornerRadius = CornerRadius(15f, 15f)
                        )
                        val path = Path().apply {
                            moveTo(topLeft.x + 50f, topLeft.y - 20)
                            lineTo(topLeft.x + 25f, topLeft.y)
                            lineTo(topLeft.x, topLeft.y - 20)
                            lineTo(topLeft.x + 50f, topLeft.y - 20)
                        }
                        drawIntoCanvas { canvas ->
                            canvas.drawOutline(
                                outline = Outline.Generic(path = path),
                                paint = androidx.compose.ui.graphics.Paint().apply {
                                    color = localLabelColor
                                })
                        }

                        drawContext.canvas.nativeCanvas.drawText(
                            item.value.toInt().toString(),
                            topLeft.x + 25,
                            topLeft.y - 50,
                            paint
                        )
                    }

                    spaceStep += spaceBetweenBars + barWidth
                }
            })
    }
}


private fun detectPosition(screenSize: Size, offset: Offset, listSize: Int, itemWidth: Float): Int {
    val spaceBetweenBars =
        (screenSize.width - (listSize * itemWidth)) / (listSize - 1)
    var spaceStep = 0f
    for (i in 0 until listSize) {
        if (offset.x in spaceStep..(spaceStep + itemWidth)) {
            return i
        }
        spaceStep += spaceBetweenBars + itemWidth
    }
    return -1
}