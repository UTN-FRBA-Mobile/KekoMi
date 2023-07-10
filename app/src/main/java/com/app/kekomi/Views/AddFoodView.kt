package com.app.kekomi.Views

import android.appwidget.AppWidgetManager
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.util.Log
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.glance.appwidget.GlanceAppWidget
import androidx.glance.appwidget.GlanceAppWidgetManager
import androidx.glance.appwidget.updateAll
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import com.app.kekomi.BottomNav.BottomNavItem
import com.app.kekomi.Extras.DateSelected
import com.app.kekomi.R
import com.app.kekomi.apis.FinalFood
import com.app.kekomi.apis.FinalNutrients
import com.app.kekomi.apis.FoodSource
import com.app.kekomi.apis.barcodeApi.BarcodeFood
import com.app.kekomi.apis.barcodeApi.getFoodByBarcode
import com.app.kekomi.apis.foodApi.ApiFoodService
import com.app.kekomi.apis.foodApi.FoodNutrients
import com.app.kekomi.apis.foodApi.FoodResponse
import com.app.kekomi.apis.foodApi.Nutrient
import com.app.kekomi.entities.Food
import com.app.kekomi.entities.Meal
import com.app.kekomi.storage.FoodRepository
import com.app.kekomi.ui.theme.principalColor
import com.app.kekomi.widget.KekoMiWidget
import com.app.kekomi.widget.KekoMiWidgetReceiver
import com.app.kekomi.widget.Widget
import kotlinx.coroutines.*
import okhttp3.MediaType
import okhttp3.RequestBody
import org.json.JSONArray
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.time.temporal.WeekFields
import java.util.*

val api_id= "b0e8bca6"
val api_key= "abef3893c7d61e39cd4f1f573733d8e8"

@Composable
fun AddFoodView(navController: NavHostController, scannedValue: String?) {

    val context = LocalContext.current
    val repo: FoodRepository by lazy {
        FoodRepository(context)
    }
    val dropdownViewModel: DropdownViewModel = viewModel()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(bottom = 50.dp)
            .verticalScroll(rememberScrollState())
            .background(Color.White)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.End

        ) {
            TextButton(
                onClick = {
                    navController.navigate(BottomNavItem.Home.screen_route)
                },

                colors = ButtonDefaults.buttonColors(backgroundColor = Color.Transparent),
            ) {
                Icon(Icons.Default.Close, contentDescription = "Localized description", tint = Color.Black)
            }
        }
        dropDownMenu( dropdownViewModel)
        SearchBar( dropdownViewModel.selectedFoodOption.value, onClear = {}, navController, scannedValue)

    }
}

@Composable
fun SearchBar(
    selectedMeal: String,
    onClear: () -> Unit,
    navController: NavHostController,
    scannedValue: String?
) {
    var text by remember { mutableStateOf("") }
    var autoCompleteResults by remember { mutableStateOf(emptyList<String>()) }
    var hadSearched by remember { mutableStateOf(false) }
    val focusManager = LocalFocusManager.current
    var scannedValueLoaded by remember { mutableStateOf(false) } // Track if scannedValue has been loaded

    if (scannedValue != "0" && !scannedValueLoaded) {
        text = scannedValue!!
        scannedValueLoaded = true
    }

    LaunchedEffect(text) {
        if(text.toDoubleOrNull() != null){
            hadSearched = true
        }else{
            autoCompleteResults = autoComplete(text)
        }
    }

    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Start,
        modifier = Modifier.padding(start = 10.dp, end = 10.dp)
    ) {
        Row(
            modifier = Modifier
                .weight(4f)
                .padding(end = 10.dp)
                .border(
                    width = 2.dp,
                    color = Color.Gray,
                    shape = RoundedCornerShape(percent = 10)
                ),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Spacer(Modifier.width(2.dp))
            Icon(
                imageVector = Icons.Filled.Search,
                contentDescription = "lupa",
                modifier = Modifier.padding(5.dp)
            )
            BasicTextField(
                value = text,
                onValueChange = { newText ->
                    text = newText
                    hadSearched = false
                },
                textStyle = TextStyle(color = Color.Black, fontSize = 25.sp),
                modifier = Modifier
                    .weight(1f)
                    .padding(end = 8.dp),
                singleLine = true,
                maxLines = 1,
                keyboardOptions = KeyboardOptions(imeAction = ImeAction.Search),
                keyboardActions = KeyboardActions(onSearch = {
                    focusManager.clearFocus()
                }),
            )
            IconButton(onClick = {
                text = ""
                onClear()
            }) {
                Icon(
                    imageVector = Icons.Default.Clear,
                    contentDescription = "Clear",
                    tint = Color.Gray
                )
            }
        }
        Row(
            modifier = Modifier
                .border(
                    width = 2.dp,
                    color = Color.Gray,
                    shape = RoundedCornerShape(percent = 10)
                )
        ) {
            IconButton(
                onClick = {
                    scannedValueLoaded = false
                    navController.navigate("CodeBarScannerView")
                }
            ) {
                Icon(
                    painter = painterResource(id = R.drawable.codigo_de_barras),
                    contentDescription = "Clear",
                    tint = Color.Gray,
                    modifier = Modifier
                        .size(35.dp)
                )
            }
        }
    }

    if(hadSearched){
        addSingleFood(text, selectedMeal, navController)
    }

    else{
        // Display the autocomplete results below the search bar
        Column(modifier = Modifier.padding(start = 15.dp, end = 10.dp)) {
            for (result in autoCompleteResults) {
                TextButton(
                    onClick = {
                        Log.d("Main", result)
                        text = result
                        hadSearched = true
                    },
                ) {
                    Text(
                        text = result,
                        fontSize = 25.sp,
                        color = Color.Black
                    )
                }
            }
        }
    }
}

class DropdownViewModel : ViewModel() {
    val foodOptions = listOf("Breakfast", "Lunch", "Dinner", "Snack")
    val selectedFoodOption = mutableStateOf(foodOptions[0])
}

@Composable
fun dropDownMenu(dropdownViewModel: DropdownViewModel) {
    val foodOptions = listOf("Breakfast", "Lunch", "Dinner", "Snack")
    val isDropdownExpanded = remember { mutableStateOf(false) }
    val selectedFoodOption = remember { mutableStateOf(foodOptions[0]) }

    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier.padding(top = 5.dp, start = 10.dp, end = 10.dp, bottom = 15.dp),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(text = "Please select the type of meal",
            style = TextStyle(fontSize = 18.sp),
            modifier = Modifier.padding(5.dp)
        )

        Box(
            modifier = Modifier
                .background(Color.LightGray)
                .border(
                    BorderStroke(1.dp, Color.LightGray),
                    shape = RoundedCornerShape(percent = 15)
                )
                .clickable { isDropdownExpanded.value = !isDropdownExpanded.value }
                .padding(5.dp)

        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.padding(horizontal = 5.dp),
                horizontalArrangement = Arrangement.End
            ) {
                Text(
                    text = selectedFoodOption.value,
                    style = TextStyle(fontSize = 20.sp)
                )
                Icon(
                    imageVector = Icons.Filled.ArrowDropDown,
                    contentDescription = "Dropdown Arrow",
                    modifier = Modifier
                        .padding(start = 5.dp)
                        .size(25.dp),
                )
            }
            DropdownMenu(
                expanded = isDropdownExpanded.value,
                onDismissRequest = { isDropdownExpanded.value = false },
                modifier = Modifier.border(
                    BorderStroke(1.dp, Color.Black),
                    shape = RoundedCornerShape(percent = 10)
                )
            ) {
                foodOptions.forEach { option ->
                    DropdownMenuItem(onClick = {
                        selectedFoodOption.value = option
                        dropdownViewModel.selectedFoodOption.value = option
                        isDropdownExpanded.value = false
                    }) {
                        Text(
                            text = option,
                            style = TextStyle(fontSize = 20.sp)
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun addSingleFood(text: String, selectedMeal: String, navController: NavHostController) {
    val food = createFood(text = text)
    val quantityState = remember { mutableStateOf(1) }

    if (food != null) {
        val nutrients = food.nutrients
        Column(
            modifier = Modifier
                .padding(20.dp)
                .fillMaxWidth()
        ) {
            if (nutrients != null) {
                showQuantitySelector(
                    initialQuantity = quantityState.value
                ) { newQuantity ->
                    quantityState.value = newQuantity
                }

                showFoodDetails("Calories", nutrients.calories, quantityState)
                showFoodDetails("Proteins", nutrients.protein, quantityState)
                showFoodDetails("Sugar", nutrients.sugar, quantityState)
                showFoodDetails("Sodium", nutrients.sodium, quantityState) // esta en mg
                showFoodDetails("Fat", nutrients.fats, quantityState)
                Spacer(modifier = Modifier.padding(20.dp))
                addButton(food, selectedMeal, navController, quantityState)
            }
        }
    } else {
        loader()
    }
}

@Composable
fun loader() {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        CircularProgressIndicator(modifier = Modifier.padding(top = 40.dp), color = principalColor)
    }
}

@Composable
fun showQuantitySelector(initialQuantity: Int, onQuantityChanged: (Int) -> Unit) {
    var quantity by remember { mutableStateOf(initialQuantity) }

    Row(
        modifier = Modifier
            .padding(bottom = 15.dp)
            .fillMaxWidth(),

        ) {
        Text(
            text = "Select Quantity",
            style = TextStyle(fontSize = 20.sp),
            modifier = Modifier
                .padding(end = 20.dp)
                .width(100.dp)
        )
        Spacer(modifier = Modifier.weight(1f))
        Row(
            modifier = Modifier
                .padding(top = 8.dp)
                .border(1.dp, Color.Black, RoundedCornerShape(10.dp))
                .width(120.dp),
            horizontalArrangement = Arrangement.Center
        )
        {
            IconButton(
                onClick = {
                    if (quantity > 1) {
                        quantity -= 1
                        onQuantityChanged(quantity)
                    }
                }
            ) {
                Icon(
                    painter = painterResource(id = R.drawable.ic_minus), // Replace with your minus icon resource
                    contentDescription = "Minus"
                )
            }

            Text(
                text = quantity.toString(),
                modifier = Modifier.align(Alignment.CenterVertically) // Center the text vertically
            )

            IconButton(
                onClick = {
                    quantity += 1
                    onQuantityChanged(quantity)
                }
            ) {
                Icon(
                    painter = painterResource(id = R.drawable.ic_plus), // Replace with your plus icon resource
                    contentDescription = "Plus"
                )
            }
        }
    }

}
@Composable
fun showFoodDetails(metricName: String, nutrient: Nutrient?, quantity: MutableState<Int>) {
    val focusManager = LocalFocusManager.current

    var finalQty by remember { mutableStateOf(nutrient?.quantity?.times(quantity.value)?.toFloat()) }
    var inputValue by remember { mutableStateOf(String.format("%.2f", finalQty)) }

    DisposableEffect(quantity.value) {
        finalQty = nutrient?.quantity?.times(quantity.value)?.toFloat()
        inputValue = String.format("%.2f", finalQty)

        onDispose {
            // Cleanup, if necessary
        }
    }

    Row(
        modifier = Modifier
            .padding(bottom = 15.dp)
            .fillMaxWidth()
    ) {
        Text(
            text = metricName,
            style = TextStyle(fontSize = 20.sp),
            modifier = Modifier
                .padding(end = 20.dp)
                .width(100.dp)
        )

        Spacer(modifier = Modifier.weight(1f))

        OutlinedTextField(
            value = inputValue,
            onValueChange = { newValue ->
                inputValue = newValue
            },
            enabled = false,
            placeholder = { Text("") },
            modifier = Modifier
                .width(120.dp)
                .height(48.dp),
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Number,
                imeAction = ImeAction.Done
            ),
            keyboardActions = KeyboardActions(
                onDone = {
                    focusManager.clearFocus()
                }
            ),
            textStyle = TextStyle(textAlign = TextAlign.Center, fontSize = 15.sp),
            visualTransformation = SuffixVisualTransformation(" ${nutrient?.unit}"),
            shape = RoundedCornerShape(10.dp),
        )
    }
}

@Composable
fun addButton(
    food: FinalFood,
    selectedMeal: String,
    navController: NavHostController,
    quantityState: MutableState<Int>
) {
    val context = LocalContext.current
    val repository = FoodRepository(context)


    Button(
        onClick = {
            repository.insertFood(
                Food(foodName = food.name,
                    calories = food.nutrients.calories?.quantity?.toInt()!!,
                    quantity = quantityState.value,
                    protein = food.nutrients.protein?.quantity?.toInt()!!,
                    sugar = food.nutrients.sugar?.quantity?.toInt()!!,
                    fats = food.nutrients.fats?.quantity?.toInt()!!,
                    sodium = food.nutrients.sodium?.quantity?.toInt()!!,
                    day = DateSelected.pickedDate.dayOfMonth,
                    week_number = DateSelected.pickedDate.get(WeekFields.of(Locale.getDefault()).weekOfWeekBasedYear()),
                    month = DateSelected.pickedDate.monthValue,
                    year = DateSelected.pickedDate.year,
                    meal = Meal.valueOf(selectedMeal.toUpperCase(Locale.ROOT)))
            )
            updateWidgets(context)
            navController.navigate(BottomNavItem.Home.screen_route)
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

fun updateWidgets(context: Context) {
    val widgetProvider = KekoMiWidgetReceiver::class.java
    val comp = ComponentName(context, widgetProvider)
    val ids = AppWidgetManager.getInstance(context)
        .getAppWidgetIds(comp)
    val intent = Intent(context, widgetProvider).apply {
        this.action = AppWidgetManager
            .ACTION_APPWIDGET_UPDATE
        this.putExtra(
            AppWidgetManager.EXTRA_APPWIDGET_IDS,
            ids
        )
    }
    context.sendBroadcast(intent)
}

fun getRetrofit(): Retrofit {
    return Retrofit.Builder()
        .baseUrl("https://api.edamam.com/")
        .addConverterFactory(GsonConverterFactory.create())
        .build()
}

fun getFood(foodName: String, callback: (FoodResponse) -> Unit) {
    if (!foodName.isNullOrBlank()){
        Log.d("ACAAA:", foodName)
        val apiService = getRetrofit().create(ApiFoodService::class.java)
        val call: Call<FoodResponse> = apiService.getFoodByName(api_id, api_key, foodName)

        call.enqueue(object : Callback<FoodResponse> {
            override fun onResponse(call: Call<FoodResponse>, response: Response<FoodResponse>) {
                if (response.isSuccessful) {
                    val foodResponse: FoodResponse? = response.body()
                    if (foodResponse != null) {

                        callback(foodResponse)
                    } else {
                        Log.e("Main", "Empty response body")
                    }
                } else {
                    Log.e("Main", "Request failed with code: ${response.code()}")
                }
            }

            override fun onFailure(call: Call<FoodResponse>, t: Throwable) {
                Log.e("Main", "Request failed: ${t.message}")
            }
        })
    }

}

fun getNutrients(foodId: String, callback: (FoodNutrients) -> Unit) {
    val apiService = getRetrofit().create(ApiFoodService::class.java)
    val ingredientsArray = JSONArray().apply {
        val ingredientObject = JSONObject().apply {
            put("quantity", 1)
            put("foodId", foodId)
        }
        put(ingredientObject)
    }

    val requestBodyJson = JSONObject().apply {
        put("ingredients", ingredientsArray)
    }.toString()

    val requestBody = RequestBody.create(MediaType.parse("application/json"), requestBodyJson)

    val call: Call<FoodNutrients> = apiService.getFoodNutrients(api_id, api_key, requestBody)

    call.enqueue(object : Callback<FoodNutrients> {
        override fun onResponse(call: Call<FoodNutrients>, response: Response<FoodNutrients>) {
            if (response.isSuccessful) {
                val foodResponse: FoodNutrients? = response.body()
                if (foodResponse != null) {
                    callback(foodResponse)
                } else {
                    Log.e("Main", "Empty response body")
                }
            } else {
                Log.e("Main", "Request failed with code: ${response.code()} and ${response.errorBody()
                    ?.string()}")
            }
        }

        override fun onFailure(call: Call<FoodNutrients>, t: Throwable) {
            Log.e("Main", "Request failed: ${t.message}")
        }
    })
}

suspend fun autoComplete(text: String): List<String> {
    return withContext(Dispatchers.IO) {
        val apiService = getRetrofit().create(ApiFoodService::class.java)
        val call: Call<List<String>> = apiService.autoComplete(api_id, api_key, text)
        val response = call.execute()

        if (response.isSuccessful) {
            response.body() ?: emptyList()
        } else {
            emptyList()
        }
    }
}

@Composable
fun createFood(text: String): FinalFood? {
    val foodResponseState = remember { mutableStateOf<FoodResponse?>(null) }
    val nutrientResponseState = remember { mutableStateOf<FoodNutrients?>(null) }
    val barcodeResponseState = remember { mutableStateOf<List<BarcodeFood>?>(null) }
    val finalFood = remember { mutableStateOf<FinalFood?>(null) }

    if(text.toDoubleOrNull() == null && text != ""){//si es null, es un texto
        getFood(text) { foodResponse ->
            foodResponseState.value = foodResponse
        }
        val foodResponse = foodResponseState.value
        if (foodResponse != null) {
            if (foodResponse.parsed.isNotEmpty()) {
                val food = getBestFit(foodResponse, text)
                getNutrients(food.foodId){foodNutrients ->
                    nutrientResponseState.value = foodNutrients
                }
                val nutrientsResponse = nutrientResponseState.value
                if(nutrientsResponse != null){
                    val calories = Nutrient(nutrientsResponse.calories.toDouble(), "kcal")
                    val totalNutrients = nutrientsResponse.totalNutrients
                    val finalNutrients = FinalNutrients(calories, totalNutrients.FAT, totalNutrients.SUGAR, totalNutrients.NA, totalNutrients.PROCNT)
                    finalFood.value = FinalFood(food.foodId, food.knownAs, nutrientsResponse.totalWeight.toDouble().toInt(), finalNutrients, FoodSource.FOODAPI)
                }
            }
        }

    }else{//aca es barcode
        getFoodByBarcode(text) { listFood ->
            barcodeResponseState.value = listFood
        }
        val barcodeResponse = barcodeResponseState.value
        if (barcodeResponse != null) {
            if (barcodeResponse.isNotEmpty()) {
                val barcodeFood = barcodeResponse.first()
                if (barcodeFood != null) {
                    finalFood.value = FinalFood(barcodeFood.id.toString(), barcodeFood.food,barcodeFood.weight, barcodeFood.nutrients,FoodSource.BARCODE)
                }
            }
        }
    }
    return finalFood.value
}

fun getBestFit(foodResponse: FoodResponse, foodName: String): com.app.kekomi.apis.foodApi.Food {

    val firstFoodName = foodResponse.parsed.first().food.knownAs

    if(firstFoodName.equals(foodName)){
        return foodResponse.parsed.first().food
    }

    for(food in foodResponse.hints){
        if(food.food.label.equals(foodName, ignoreCase = true) || food.food.knownAs.equals(foodName, ignoreCase = true)  ){
            return  food.food
        }
    }

    return foodResponse.parsed.first().food
}








