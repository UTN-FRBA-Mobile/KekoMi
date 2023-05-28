package com.app.kekomi.Views

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
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
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
import com.app.kekomi.storage.FoodRepository
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


val api_id= "b0e8bca6"
val api_key= "abef3893c7d61e39cd4f1f573733d8e8"


@Composable
fun AddFoodView(navController: NavHostController) {


    val context = LocalContext.current
    val repo: FoodRepository by lazy {
        FoodRepository(context)
    }


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
                    navController.popBackStack()
                },

                colors = ButtonDefaults.buttonColors(backgroundColor = Color.Transparent),
            ) {
                Icon(Icons.Default.Close, contentDescription = "Localized description", tint = Color.Black)
            }
//            TextButton(
//                onClick = {
//                    autoComplete("Ban")
//                },
//                colors = ButtonDefaults.buttonColors(backgroundColor = Color.Transparent),
//            ) {
//                Icon(Icons.Default.Add, contentDescription = "Localized description", tint = Color.Black)
//            }
        }
        dropDownMenu()

        SearchBar( onClear = {}, navController)

    }
}



@Composable
fun SearchBar(
    onClear: () -> Unit,
    navController: NavHostController
) {
    var text by remember { mutableStateOf("") }
    var autoCompleteResults by remember { mutableStateOf(emptyList<String>()) }
    var hadSearched by remember { mutableStateOf(false) }
    val focusManager = LocalFocusManager.current

    LaunchedEffect(text) {
        // Delay
        // delay(500)
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
        addSingleFood(text)
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
                        color = Color.Black,
                        textAlign = TextAlign.Center
                    )

                }
            }



        }
    }
}
@Preview
@Composable
fun dropDownMenu() {
    val foodOptions = listOf("Breakfast", "Lunch", "Dinner", "Snacks")
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
fun addSingleFood(text: String) {

    val foodResponseState = remember { mutableStateOf<FoodResponse?>(null) }
    val nutrientResponseState = remember { mutableStateOf<FoodNutrients?>(null) }


    getFood(text) { foodResponse: FoodResponse ->
        foodResponseState.value = foodResponse
    }

    val foodResponse = foodResponseState.value


    if (foodResponse != null) {
//        Text("${foodResponse.parsed.joinToString(",")}")
        getNutrients(foodResponse.parsed.first().food.foodId) { foodNutrients ->
            nutrientResponseState.value = foodNutrients
        }
        val nutrientsResponse = nutrientResponseState.value
        if (nutrientsResponse != null) {

            Column(
                modifier = Modifier
                    .padding(20.dp)
                    .fillMaxWidth()
            ) {
                showFoodDetails("Calories", nutrientResponseState.value!!.calories)
                showFoodDetails("Proteins", nutrientResponseState.value!!.totalNutrients.PROCNT.quantity.toString())
                showFoodDetails("Sugar", nutrientResponseState.value!!.totalNutrients.SUGAR.quantity.toString())
                showFoodDetails("Sodium", nutrientResponseState.value!!.totalNutrients.NA.quantity.toString()) //esta en mg
                showFoodDetails("Fat", nutrientResponseState.value!!.totalNutrients.FAT.quantity.toString() )
            }

        }

    }
}
@Composable
fun showFoodDetails(metricName: String, metric: String) {
    val focusManager = LocalFocusManager.current
    var inputValue by remember { mutableStateOf(metric) }


        Row(
            modifier = Modifier.padding(bottom = 15.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = metricName,
                style = TextStyle(fontSize = 20.sp),
                modifier = Modifier.padding(end = 10.dp)
            )

            Box(modifier = Modifier.weight(1f)) {
                OutlinedTextField(
                    value = inputValue,
                    onValueChange = { newValue ->
                       inputValue = newValue
                    },
                    placeholder = { Text("") },
                    modifier = Modifier
                        .padding(start = 50.dp, end = 10.dp)
                        .width(100.dp)
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
                    visualTransformation = SuffixVisualTransformation(" g"),
                    shape = RoundedCornerShape(10.dp),
                )
            }
        }

}





fun getRetrofit(): Retrofit {
    return Retrofit.Builder()
        .baseUrl("https://api.edamam.com/")
        .addConverterFactory(GsonConverterFactory.create())
        .build()
}

fun getFood(foodName: String, callback: (FoodResponse) -> Unit) {
    val apiService = getRetrofit().create(ApiFoodService::class.java)
    val call: Call<FoodResponse> = apiService.getFoodByName(api_id, api_key, foodName)

    call.enqueue(object : Callback<FoodResponse> {
        override fun onResponse(call: Call<FoodResponse>, response: Response<FoodResponse>) {
            if (response.isSuccessful) {
                val foodResponse: FoodResponse? = response.body()
                if (foodResponse != null) {
                    Log.d("Main", "Success! $foodResponse")
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
                    Log.d("Main", "Success! $foodResponse")
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




//suspend fun getFood(foodName: String): FoodResponse? {
//    return withContext(Dispatchers.IO) {
//        val apiService = getRetrofit().create(ApiFoodService::class.java)
//        val call: Call<FoodResponse> = apiService.getFoodByName(api_id, api_key, foodName)
//        val response = call.execute()
//
//        if (response.isSuccessful) {
//            val responseBody = response.body()
//            Log.d("Main:", responseBody.toString())
//            responseBody
//        } else {
//            Log.e("Main:", "Failed to fetch food data: ${response.code()}")
//            null
//        }
//    }
//}




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
//        Text("${foodResponse.parsed.joinToString(",")}")
            getNutrients(foodResponse.parsed.first().food.foodId){foodNutrients ->
                nutrientResponseState.value = foodNutrients
            }
            val nutrientsResponse = nutrientResponseState.value
            if(nutrientsResponse != null){
                val food = foodResponse.parsed.first().food
                val calories = Nutrient(nutrientsResponse.calories.toDouble(), "kcal")
                val totalNutrients = nutrientsResponse.totalNutrients
                val finalNutrients = FinalNutrients(calories, totalNutrients.FAT, totalNutrients.SUGAR, totalNutrients.NA, totalNutrients.PROCNT)
                finalFood.value = FinalFood(food.foodId, food.label, nutrientsResponse.totalWeight.toDouble().toInt(), finalNutrients, food.image, FoodSource.FOODAPI)
            }
        }

    }else{//aca es barcode
        Log.d("BARCODE:", "hay barcode")
        getFoodByBarcode(text) { listFood ->
            barcodeResponseState.value = listFood
        }
        val barcodeResponse = barcodeResponseState.value
        if (barcodeResponse != null) {
            val barcodeFood = barcodeResponse?.find{it.barcode === text}
            if (barcodeFood != null) {
                finalFood.value = FinalFood(barcodeFood.id.toString(), barcodeFood.food,barcodeFood.weight, barcodeFood.nutrients,"",
                    FoodSource.BARCODE)
            }
        }
    }
    Log.d("Main::", "${finalFood.value}")
    return finalFood.value
}








