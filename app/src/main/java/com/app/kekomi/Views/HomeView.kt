package com.app.kekomi.Views

import androidx.compose.ui.Modifier
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
//import androidx.compose.foundation.layout.BoxScopeInstance.align
import androidx.compose.foundation.layout.Column
//import androidx.compose.foundation.layout.ColumnScopeInstance.weight
import androidx.compose.foundation.layout.Row
//import androidx.compose.foundation.layout.RowScopeInstance.weight
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.LinearProgressIndicator
import androidx.compose.material.Text
import androidx.compose.material.TopAppBar
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.ArrowForward
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.app.kekomi.Extras.DatePicker2
import com.app.kekomi.Extras.DonutChart

@Composable
fun HomeView(navController: NavHostController) {
    Column {
        TopAppBar(
            backgroundColor = Color(android.graphics.Color.parseColor("#008080")),
            navigationIcon = {
                IconButton(onClick = { /*TODO DIA ANTES*/}) {
                    Icon(imageVector = Icons.Filled.ArrowBack, contentDescription = "Cambia fecha para atras")
                }
            },
            title = { DatePicker2() },
            actions = {
                IconButton(onClick = { /*TODO DIA DPS*/}) {
                    Icon(imageVector = Icons.Filled.ArrowForward, contentDescription = "Cambia fecha para adelante")
                }
            }
        )

        Box(modifier = Modifier.fillMaxSize()) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .verticalScroll(rememberScrollState())
                    .padding(start = 10.dp, bottom = 50.dp, top = 20.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Row() {
                    Box(
                        modifier = Modifier
                            .weight(1f)
                            .padding(16.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        DonutChart()
                        /*val data = listOf(20f, 30f, 50f)
                        val colors = listOf(Color.Blue, Color.Green, Color.Red)
                        val labels = listOf("Label 1", "Label 2", "Label 3")
                        Box(modifier = Modifier.fillMaxSize()) {
                            DonutChart(data = data, colors = colors, labels = labels)
                        }*/
                    }
                    Column(
                        modifier = Modifier
                            .weight(1f)
                            .padding(16.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        ProgressBarWithText(0.5f)
                        ProgressBarWithText(0.5f)
                        ProgressBarWithText(0.5f)
                    }
                }
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp)
                        .wrapContentHeight()
                        .align(Alignment.CenterHorizontally),
                    contentAlignment = Alignment.Center
                ) {
                    Button(
                        onClick = {
                            navController.navigate("AddFoodView")
                        },
                        colors = ButtonDefaults.buttonColors(
                            backgroundColor = Color(android.graphics.Color.parseColor("#008080"))
                        ),
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(80.dp)
                            .padding(16.dp)
                    ) {
                        Text("+     Add food", fontSize = 20.sp, color = Color.White)
                    }
                }
                Column() {
                    FoodGroup("Breakfast", navController)
                    Spacer(Modifier.height(9.dp))
                    FoodGroup("Lunch", navController)
                    Spacer(Modifier.height(9.dp))
                    FoodGroup("Dinner", navController)
                    Spacer(Modifier.height(9.dp))
                    FoodGroup("Snacks", navController)
                }
            }
        }
    }
}


@Composable
fun ProgressBarWithText(percentage: Float) {
    val progress by remember { mutableStateOf(percentage) }

    Text("Progress: ${progress * 100}%", fontWeight = FontWeight.Bold)
    Spacer(modifier = Modifier.height(4.dp))
    LinearProgressIndicator(
                progress = progress,
                modifier = Modifier.width(200.dp),
                color = Color(android.graphics.Color.parseColor("#008080"))
    )
    Spacer(modifier = Modifier.height(10.dp))
}

@Composable
fun FoodGroup(foodGroup:String, navController: NavHostController){
//TODO esta funcion va a hacer que por cada item en la lista de desayuno por ej, se agregue un boton. For each

    Text(text = foodGroup,fontSize = 20.sp, color = Color.Black)
    Spacer(Modifier.height(6.dp))
    FoodButton(navController)

}@Composable
fun FoodButton(navController: NavHostController) {
    Button(
        onClick = { navController.navigate("FoodDetailsView") },
        colors = ButtonDefaults.buttonColors(backgroundColor = Color.White),
        modifier = Modifier
            .width(330.dp)
            .height(50.dp)
            .border(3.dp, Color(0xFF008080), RoundedCornerShape(15))
    ) {

            Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.Start) {
                Column (Modifier.align(Alignment.CenterVertically)){
                    //TODO HABRIA QUE VER SI SE PUEDE PONER ALGUNA FOTO PERO NO CREO
                    Text(text = "FOOD NAME", fontWeight = FontWeight.Bold)
                }
                Spacer(modifier = Modifier.width(120.dp))
                Column {
                    Text(text = "XX UNITS", fontSize = 13.sp)
                    Spacer(Modifier.height(2.dp))
                    Text(text = "XXXX KCAL", fontSize = 13.sp)
                }
            }

    }
    Spacer(Modifier.height(6.dp))
}
