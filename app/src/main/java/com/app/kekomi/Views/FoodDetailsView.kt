package com.app.kekomi.Views

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.app.kekomi.entities.Food
import com.app.kekomi.storage.FoodRepository
import com.app.kekomi.storage.userPreferences
import kotlinx.coroutines.CoroutineScope

@Composable
fun FoodDetailsView(navController: NavHostController, foodId: Int) {

    val context = LocalContext.current
    val repository = FoodRepository(context)
    val food = remember { mutableStateOf(repository.getFood(foodId)) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(start = 10.dp, bottom = 50.dp)
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
                Icon(
                    Icons.Default.Close,
                    contentDescription = "Localized description",
                    tint = Color.Black
                )
            }
        }
        Text(text = food.value.foodName)
        Item(food, NutritionItem.CALORIES)
        Item(food, NutritionItem.SODIUM)
        Item(food, NutritionItem.SUGAR)
        Item(food, NutritionItem.FATS)
        Item(food, NutritionItem.PROTEINS)
        TextButton(
            onClick = {
                repository.updateFood(food.value)
            },
            colors = ButtonDefaults.buttonColors(backgroundColor = Color.Transparent),
        ) {
            Icon(
                Icons.Default.Close,
                contentDescription = "Localized description",
                tint = Color.Black
            )
        }
    }
}

enum class NutritionItem {
    CALORIES,
    SODIUM,
    SUGAR,
    FATS,
    PROTEINS
}

@Composable
fun Item(
    food: MutableState<Food>,
    nutritionItem: NutritionItem
){
    val initialValue = when (nutritionItem) {
        NutritionItem.CALORIES -> food.value.calories
        NutritionItem.SODIUM -> food.value.sodium
        NutritionItem.SUGAR -> food.value.sugar
        NutritionItem.FATS -> food.value.fats
        NutritionItem.PROTEINS -> food.value.protein
    }

    var inputValueG by remember { mutableStateOf(initialValue)}

    LaunchedEffect(initialValue) {
        inputValueG = initialValue
    }

    val outlineTextFieldColors = TextFieldDefaults.outlinedTextFieldColors(
        focusedBorderColor = Color(0xFF008080), // change the border color when focused
        textColor = Color.Black, // change the text color
        focusedLabelColor = Color(0xFF008080),
        unfocusedBorderColor = Color.Gray,
        disabledBorderColor = Color.Gray
    )
    val focusManager = LocalFocusManager.current
    OutlinedTextField(
        value = inputValueG.toString(),
        onValueChange = { newValue -> inputValueG = newValue.toInt() },
        label = { Text("Set goal", fontSize = 15.sp, textAlign = TextAlign.Center) },
        placeholder = { Text("") },
        modifier = Modifier
            .padding(end = 10.dp)
            .width(100.dp)
            .height(55.dp),
        keyboardOptions = KeyboardOptions.Default.copy(
            keyboardType = KeyboardType.Number,
            imeAction = ImeAction.Done
        ),
        keyboardActions = KeyboardActions(
            onDone = {
                focusManager.clearFocus()
                when (nutritionItem) {
                    NutritionItem.CALORIES -> food.value.calories = inputValueG.toInt()
                    NutritionItem.SODIUM -> food.value.sodium = inputValueG.toInt()
                    NutritionItem.SUGAR -> food.value.sugar = inputValueG.toInt()
                    NutritionItem.FATS -> food.value.fats = inputValueG.toInt()
                    NutritionItem.PROTEINS -> food.value.protein = inputValueG.toInt()
                }
            }
        ),
        textStyle = TextStyle(textAlign = TextAlign.End, fontSize = 15.sp),
       /* visualTransformation = if (item != "Calories") {
            SuffixVisualTransformation(" g")
        } else {
            SuffixVisualTransformation("  ")
        },*/
        colors = outlineTextFieldColors,
        shape = RoundedCornerShape(percent = 10)
    )
}