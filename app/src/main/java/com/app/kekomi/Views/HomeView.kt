package com.app.kekomi.Views

//import androidx.compose.foundation.layout.BoxScopeInstance.align
//import androidx.compose.foundation.layout.ColumnScopeInstance.weight
//import androidx.compose.foundation.layout.RowScopeInstance.weight
import android.util.Log
import androidx.compose.ui.Modifier
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
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
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.app.kekomi.Extras.DatePicker
import com.app.kekomi.Extras.DateSelected
import com.app.kekomi.Extras.DonutChart
import com.app.kekomi.Extras.decrementDay
import com.app.kekomi.Extras.incrementDate
import com.app.kekomi.Extras.showDate
import com.app.kekomi.entities.Food
import com.app.kekomi.entities.Meal
import com.app.kekomi.storage.FoodRepository
import com.app.kekomi.storage.userPreferences
import java.time.ZoneId
import java.util.*
import kotlin.math.roundToInt

@Composable

fun HomeView(navController: NavHostController) {

    val context = LocalContext.current

    Column {
        TopAppBar(
            backgroundColor = Color(android.graphics.Color.parseColor("#008080")),
            navigationIcon = {
                DatePicker()
            },
            title = {
                Row(
                    modifier = Modifier.padding(start = 5.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.Center
                ) {
                    IconButton(onClick = { decrementDay()}) {
                        Icon(imageVector = Icons.Filled.ArrowBack, contentDescription = "Cambia fecha para atrás",tint = Color.White)
                    }
                    showDate()
                    IconButton(onClick = { incrementDate()}) {
                        Icon(imageVector = Icons.Filled.ArrowForward, contentDescription = "Cambia fecha para adelante", tint = Color.White)
                    }
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
                    }
                    Column(
                        modifier = Modifier
                            .weight(1f)
                            .padding(16.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        progressBars()

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
                    var food = remember { mutableStateListOf<Food>() }
                    LaunchedEffect(DateSelected.pickedDate){
                        food.clear()
                        food.addAll(FoodRepository(context).getAllFood(
                            Date.from(DateSelected.pickedDate.atStartOfDay(ZoneId.systemDefault())
                                .toInstant())))
                        Log.d("comidita",food.filter { food -> food.meal == Meal.BREAKFAST }.toString())
                    }
                    FoodGroup("Breakfast", food.filter { food -> food.meal == Meal.BREAKFAST }.toMutableStateList(), navController)
                    Spacer(Modifier.height(9.dp))
                    FoodGroup("Lunch", food.filter { food -> food.meal == Meal.LUNCH }.toMutableStateList(), navController)
                    Spacer(Modifier.height(9.dp))
                    FoodGroup("Dinner", food.filter { food -> food.meal == Meal.DINNER }.toMutableStateList(), navController)
                    Spacer(Modifier.height(9.dp))
                    FoodGroup("Snacks", food.filter { food -> food.meal == Meal.SNACK }.toMutableStateList(), navController)
                    Spacer(Modifier.height(15.dp))
                }
            }

        }
    }
}

@Composable
fun getGoals(dataStore: userPreferences): List<String> {
    val metrics = getCheckedItems(dataStore = dataStore)

    return metrics.map { metric ->
        dataStore.getGoalFromKey(metric).collectAsState(initial = "").value!!
    }
}

@Composable
fun getCheckedItems(dataStore: userPreferences): List<String> {
    val items = dataStore.getItems()
    var states = dataStore.getStates.map { item -> item.collectAsState(initial = "").value!!}
    states = states.map{ item -> item.toString().toBoolean() }


    val checkedItems = items.take(items.size).filterIndexed() { index, _ -> states[index] }
    return checkedItems

}
@Composable
fun progressBars() {
    val context = LocalContext.current
    // scope
    val scope = rememberCoroutineScope()
    // datastore
    val dataStore = userPreferences(context)

    val metrics = getCheckedItems(dataStore)
    val goals = getGoals(dataStore = dataStore)

    for (metric in metrics) {
        val index = metrics.indexOf(metric)
        val goal = goals[index]
        Log.d("goal", goal)
        val value = 20
        var percentage by remember { mutableStateOf(0f) }

        LaunchedEffect(goal) {
            if (goal.isNotEmpty()) {
                percentage = (value * 100 / goal.toInt()).toFloat()
                Log.d("p", percentage.toString())
            }
        }
        ProgressBarWithText(percentage, metric)
    }
}

@Composable
fun ProgressBarWithText(percentage: Float, label: String) {
    var progress by remember { mutableStateOf(percentage) }

    LaunchedEffect(percentage) {
        progress = percentage
    }

    Text("${label}: ${progress }%", fontWeight = FontWeight.Bold)
    Spacer(modifier = Modifier.height(4.dp))
    LinearProgressIndicator(
        progress = progress/100,
        modifier = Modifier.width(200.dp),
        color = Color(android.graphics.Color.parseColor("#008080"))
    )
    Spacer(modifier = Modifier.height(10.dp))
}

@Composable
fun FoodGroup(foodGroup:String, food: MutableList<Food>, navController: NavHostController){
//TODO esta funcion va a hacer que por cada item en la lista de desayuno por ej, se agregue un boton. For each
    Log.d("comiditaAdentrito",food.toString())
    Text(text = foodGroup,fontSize = 20.sp, color = Color.Black)
    Spacer(Modifier.height(6.dp))
    if(food.isEmpty()){
        Text(text = "You have not eaten yet",fontSize = 12.sp, color = Color.Black)
    }else {
        food.forEach{ it ->
            FoodButton(it, navController)
        }
    }

}@Composable
fun FoodButton(food: Food, navController: NavHostController) {
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
                    Text(text = food.foodName, fontWeight = FontWeight.Bold)
                }
                Spacer(modifier = Modifier.width(120.dp))
                Column {
                    Text(text = food.quantity.toString() + " UNITS", fontSize = 13.sp)
                    Spacer(Modifier.height(2.dp))
                    Text(text = food.calories.toString() + " KCAL", fontSize = 13.sp)
                }
            }

    }
    Spacer(Modifier.height(6.dp))
}
