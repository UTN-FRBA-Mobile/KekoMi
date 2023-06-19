package com.app.kekomi.Views

//import androidx.compose.foundation.layout.BoxScopeInstance.align
//import androidx.compose.foundation.layout.ColumnScopeInstance.weight
//import androidx.compose.foundation.layout.RowScopeInstance.weight
import android.util.Log
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
//import androidx.compose.foundation.layout.RowScopeInstance.align
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.ArrowForward
import androidx.compose.material.icons.filled.Home
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.app.kekomi.Extras.*
import com.app.kekomi.entities.Food
import com.app.kekomi.entities.Meal
import com.app.kekomi.storage.FoodRepository
import com.app.kekomi.storage.userPreferences
import com.app.kekomi.ui.theme.principalColor
import java.time.temporal.WeekFields
import java.util.*

@Composable

fun HomeView(navController: NavHostController) {

    val context = LocalContext.current
    val repository = FoodRepository(context)
    val food = remember { mutableStateListOf<Food>() }

    Column {
        TopAppBar(
            backgroundColor = principalColor,
            navigationIcon = {
                DatePicker()
            },
            title = {
                Row(
                    //modifier = Modifier.padding(start = 5.dp),
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
            },
            actions = {
                IconButton(onClick = { todayDate()}) {
                    Icon(imageVector = Icons.Filled.Home, contentDescription = "Cambia fecha a la actual", tint = Color.White)
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
                            .padding(start = 16.dp, end = 20.dp),
                        horizontalAlignment = Alignment.Start

                    ) {
                        Text("Goals:", fontSize = 26.sp, color = Color.Black, modifier=Modifier.padding(bottom = 10.dp))
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
                            val scanned = "0"
                            navController.navigate("AddFoodView/$scanned")
                        },
                        colors = ButtonDefaults.buttonColors(
                            backgroundColor = principalColor
                        ),
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(80.dp)
                            .padding(16.dp)
                    ) {
                        Text("+     Add food", fontSize = 20.sp, color = Color.White)
                    }
                }
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(start = 20.dp),
                    horizontalAlignment = Alignment.Start
                ) {
                    LaunchedEffect(DateSelected.pickedDate){
                        food.clear()
                        food.addAll(repository.getAllFood(DateSelected.pickedDate))
                        val weekstats = repository.getWeekStats()
                        Log.d("stats", weekstats.toString())
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

    val repository = FoodRepository(context)

    val metrics = getCheckedItems(dataStore)
    val goals = getGoals(dataStore = dataStore)


    for (metric in metrics) {
        val index = metrics.indexOf(metric)
        val goal = goals[index]
        Log.d("goal", goal)

        val stats = repository.getStatsFrom(DateSelected.pickedDate)

        val value: Float = when (metric) {
            "Calories" -> stats?.calories?.toFloat() ?: 0f
            "Proteins" -> stats?.protein?.toFloat() ?: 0f
            "Fats" -> stats?.fats?.toFloat() ?: 0f
            "Sodium" -> stats?.sodium?.toFloat() ?: 0f
            "Sugar" -> stats?.sugar?.toFloat() ?: 0f
            else -> 0f
        }
        Log.d(metric, "${value}")

        var percentage by remember { mutableStateOf(0f) }


            if (goal.isNotEmpty()) {
                val goalF = goal.toFloat()
                if(value < goalF){
                    var percentageDec = (value * 100 / goalF).toFloat()
                    percentage = (Math.round(percentageDec * 10.0) / 10.0).toFloat()
                    Log.d("p", percentage.toString())
                }
                else{
                    percentage = 100F
                }
            }

        Log.d("${metric}PP", "${percentage}")
        //SI NO HAY GOAL, NO SE DEBERIA MOSTRAR LA BARRA.
        if (goal.isNotEmpty()) {
            ProgressBarWithText(percentage, metric)
        }
    }
}

@Composable
fun ProgressBarWithText(percentage: Float, label: String) {
    var progress by remember { mutableStateOf(percentage) }

    LaunchedEffect(percentage) {
        progress = percentage
    }

    Text("${label}: ${progress }%", fontWeight = FontWeight.Bold, textAlign = TextAlign.Start)
    Spacer(modifier = Modifier.height(4.dp))
    LinearProgressIndicator(
        progress = progress/100,
        modifier = Modifier.width(200.dp),
        color = principalColor
    )
    Spacer(modifier = Modifier.height(10.dp))
}

@Composable
fun FoodGroup(foodGroup:String, food: MutableList<Food>, navController: NavHostController){
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
        onClick = {
            navController.navigate("FoodDetailsView/" + food.foodId)
                  },
        colors = ButtonDefaults.buttonColors(backgroundColor = Color.White),
        modifier = Modifier
            .fillMaxWidth()
            .height(50.dp)
            .padding(end = 30.dp)
            .border(3.dp, principalColor, RoundedCornerShape(15))
    ) {

            Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.Start) {
                Column (
                    Modifier
                        .weight(1f)
                        .align(Alignment.CenterVertically)){

                    Text(text = food.foodName, fontWeight = FontWeight.Bold)
                }
                //Spacer(modifier = Modifier.width(120.dp))
                Column (
                    horizontalAlignment = Alignment.End
                        ){
                    Text(text = food.quantity.toString() + " UNITS", fontSize = 13.sp)
                    Spacer(Modifier.height(2.dp))
                    Text(text = food.calories.toString() + " KCAL", fontSize = 13.sp)
                }
            }

    }
    Spacer(Modifier.height(6.dp))
}
