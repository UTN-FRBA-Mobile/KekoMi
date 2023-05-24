package com.app.kekomi.Views

import android.annotation.SuppressLint
import android.util.Log
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.ClickableText
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
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.app.kekomi.R
import com.app.kekomi.apis.foodApi.ApiFoodService
import com.app.kekomi.apis.foodApi.FoodResponse
import com.app.kekomi.storage.FoodRepository
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
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
    val focusManager = LocalFocusManager.current

    LaunchedEffect(text) {
        // Delay for autocomplete when user stops typing
       // delay(500) // Adjust the delay time as needed

        // Call the autoComplete function and update the results
        autoCompleteResults = autoComplete(text)
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

    // Display the autocomplete results below the search bar
    Column(modifier = Modifier.padding(start = 15.dp, end = 10.dp)) {
        for (result in autoCompleteResults) {
            TextButton(
                onClick = {
                    Log.d("Main", result)
                    text = result
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


private fun getRetrofit(): Retrofit {
    return Retrofit.Builder()
        .baseUrl("https://api.edamam.com/")
        .addConverterFactory(GsonConverterFactory.create())
        .build()
}

fun getFood(foodName: String) {
    val apiService = getRetrofit().create(ApiFoodService::class.java)
    val call: Call<FoodResponse> = apiService.getFoodByName(api_id, api_key, foodName)

    call.enqueue(object : Callback<FoodResponse> {
        override fun onResponse(call: Call<FoodResponse>, response: Response<FoodResponse>) {
            if (response.isSuccessful) {
                val foodResponse: FoodResponse? = response.body()
                // Process the foodResponse here
                Log.d("Main", "Success! $foodResponse")
            } else {
                Log.e("Main", "Request failed with code: ${response.code()}")
            }
        }

        override fun onFailure(call: Call<FoodResponse>, t: Throwable) {
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









