package com.app.kekomi.Extras

import android.graphics.Paint
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
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

private val defaultMaxHeight = 200.dp


@Preview
@Composable
fun testBarChart2(){
    val dataList = mutableListOf(10,15,10,20,70)
    val floatValue = mutableListOf<Float>()
    //val datesList = mutableListOf(2,3,4,5,6)

    dataList.forEachIndexed { index, value ->

        floatValue.add(index = index, element = value.toFloat()/dataList.max().toFloat())

    }

    barChart2(
        graphBarData = floatValue,
        //xAxisScaleData = datesList,
        barData_ = dataList,
        // height = 300.dp,
        // roundType = BarType.TOP_CURVED,
        //barWidth = 20.dp,
        //barColor = Purple500,
        //barArrangement = Arrangement.SpaceEvenly
    )
}



@Composable
fun barChart2(
    graphBarData: List<Float> = listOf(65f, 40f, 25f, 20f,10f,40f,30f,50f,80f,90f,40f,27f),
    xAxisScaleData: List<String> = listOf("Jan","Feb", "Mar", "Apr", "May", "Jun", "Jul", "Aug", "Sep", "Oct", "Nov","Dec"),
    barData_: List<Int> = listOf(65, 40, 25, 20,10,40,30,50,10,90,40,27),
    height: Dp = 200.dp,
    roundType: BarType = BarType.TOP_CURVED,
    barWidth: Dp = 20.dp,
    barColor: Color = Color(android.graphics.Color.parseColor("#006c6c")),
    barArrangement: Arrangement.Horizontal = Arrangement.SpaceEvenly
) {
    val barData by remember {
        mutableStateOf(barData_+0)
    }

    // for getting screen width and height you can use LocalConfiguration
    val configuration = LocalConfiguration.current
    // getting screen width
    val width = configuration.screenWidthDp.dp

    // bottom height of the X-Axis Scale
    val xAxisScaleHeight = 40.dp

    val yAxisScaleSpacing by remember {
        mutableStateOf(100f)
    }
    val yAxisTextWidth by remember {
        mutableStateOf(100.dp)
    }

    // bar shape
    val barShap =
        when (roundType) {
            BarType.CIRCULAR_TYPE -> CircleShape
            BarType.TOP_CURVED -> RoundedCornerShape(topStart = 5.dp, topEnd = 5.dp)
        }

    val density = LocalDensity.current
    // y-axis scale text paint
    val textPaint = remember(density) {
        Paint().apply {
            color = Color.Black.hashCode()
            textAlign = Paint.Align.CENTER
            textSize = density.run { 12.sp.toPx() }
        }
    }

    // for y coordinates of y-axis scale to create horizontal dotted line indicating y-axis scale
    val yCoordinates = mutableListOf<Float>()
    // for dotted line effect
    val pathEffect = PathEffect.dashPathEffect(floatArrayOf(10f, 10f), 0f)
    // height of vertical line over x-axis scale connecting x-axis horizontal line
    val lineHeightXAxis = 10.dp
    // height of horizontal line over x-axis scale
    val horizontalLineHeight = 5.dp

    Box(
        modifier = Modifier.fillMaxWidth(),
        contentAlignment = Alignment.TopStart
    ) {

        // y-axis scale and horizontal dotted lines on graph indicating y-axis scale
        Column(
            modifier = Modifier
                .padding(top = xAxisScaleHeight, end = 3.dp)
                .height(height)
                .fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            Canvas(modifier = Modifier
                .padding(bottom = 10.dp)
                .fillMaxSize()) {

                // Y-Axis Scale Text
                val yAxisScaleText = 100 / 4f
                (0..4).forEach { i ->
                    drawContext.canvas.nativeCanvas.apply {
                        drawText(
                            (0+i*25).toString(),
                            25f,
                            size.height - yAxisScaleSpacing - i * size.height / 4f,
                            textPaint
                        )
                    }
                    yCoordinates.add(size.height - yAxisScaleSpacing - i * size.height / 4f)
                }

                // horizontal dotted lines on graph indicating y-axis scale
                (1..4).forEach {
                    drawLine(
                        start = Offset(x = yAxisScaleSpacing +30f, y = yCoordinates[it]),
                        end = Offset(x= size.width, y = yCoordinates[it]),
                        color = Color.Gray,
                        strokeWidth = 5f,
                        pathEffect = pathEffect
                    )
                }

            }

        }

        // Graph with Bar Graph and X-Axis Scale
        Box(
            modifier = Modifier
                .padding(start = 50.dp)
                .width(width - yAxisTextWidth)
                .height(height + xAxisScaleHeight),
            contentAlignment = Alignment.BottomCenter
        ) {

            Row(
                modifier = Modifier
                    .width(width - yAxisTextWidth),
                verticalAlignment = Alignment.Top,
                horizontalArrangement = barArrangement
            ) {

                // Graph
                graphBarData.forEachIndexed { index, value ->
                    /*
                    var animationTriggered by remember {
                        mutableStateOf(false)
                    }
                    val graphBarHeight by animateFloatAsState(
                        targetValue = if (animationTriggered) value else 0f,
                        animationSpec = tween(
                            durationMillis = 1000,
                            delayMillis = 0
                        )
                    )
                    LaunchedEffect(key1 = true) {
                        animationTriggered = true
                    }
*/
                    Column(
                        modifier = Modifier.fillMaxHeight(),
                        verticalArrangement = Arrangement.Top,
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        var itemHeight = value * defaultMaxHeight.value / 100
                        // Each Graph
                        Box(
                            modifier = Modifier
                                .padding(bottom = 5.dp)
                                .clip(barShap)
                                .width(barWidth)
                                .height(height - 10.dp)
                                .background(Color.Transparent),
                            contentAlignment = Alignment.BottomCenter
                        ) {
                            Box(
                                modifier = Modifier
                                    .clip(barShap)
                                    .fillMaxWidth()
                                    .fillMaxHeight(itemHeight)
                                    .background(barColor)
                            )
                        }

                        // scale x-axis and bottom part of graph
                        Column(
                            modifier = Modifier
                                .height(xAxisScaleHeight),
                            verticalArrangement = Arrangement.Top,
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {

                            // small vertical line joining the horizontal x-axis line
                            Box(
                                modifier = Modifier
                                    .clip(
                                        RoundedCornerShape(
                                            bottomStart = 2.dp,
                                            bottomEnd = 2.dp
                                        )
                                    )
                                    .width(horizontalLineHeight)
                                    .height(lineHeightXAxis)
                                    .background(color = Color.Gray)
                            )
                            // scale x-axis
                            Text(
                                modifier = Modifier.padding(bottom = 3.dp),
                                text = xAxisScaleData[index],
                                fontSize = 14.sp,
                                fontWeight = FontWeight.Medium,
                                textAlign = TextAlign.Center,
                                color = Color.Black
                            )

                        }

                    }

                }

            }

            // horizontal line on x-axis below the graph
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(Color.Transparent),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {

                Box(
                    modifier = Modifier
                        .padding(bottom = xAxisScaleHeight + 3.dp)
                        .clip(RoundedCornerShape(2.dp))
                        .fillMaxWidth()
                        .height(horizontalLineHeight)
                        .background(Color.Gray)
                )

            }


        }


    }
}

enum class BarType {

    CIRCULAR_TYPE,
    TOP_CURVED

}