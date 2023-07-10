package com.app.kekomi.Views

import android.content.Context
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
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.app.kekomi.R
import com.app.kekomi.entities.Food
import com.app.kekomi.storage.FoodRepository
import com.app.kekomi.ui.theme.Teal200
import com.app.kekomi.ui.theme.principalColor

@Composable
fun FoodDetailsView(navController: NavHostController, foodId: Int) {

    val context = LocalContext.current
    val repository = FoodRepository(context)
    val food = remember { mutableStateOf(repository.getFood(foodId)) }
    val showAlert = remember { mutableStateOf(false) }
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
        Text(
            text = food.value.foodName.uppercase(),
            textAlign = TextAlign.Center,
            fontSize = 30.sp,
            modifier = Modifier
                .padding(bottom = 10.dp)
                .fillMaxWidth()
        )
        Item(food, NutritionItem.CALORIES)
        Item(food, NutritionItem.SODIUM)
        Item(food, NutritionItem.SUGAR)
        Item(food, NutritionItem.FATS)
        Item(food, NutritionItem.PROTEINS)
        Spacer(modifier = Modifier.weight(1f))
        OutlinedButton(
            onClick = {
                repository.updateFood(food.value)
                updateWidgets(context)
            },
            colors = ButtonDefaults.buttonColors(backgroundColor = principalColor),
            modifier = Modifier
                .fillMaxWidth()
                .padding(start = 40.dp, end = 50.dp, bottom = 10.dp)
        ) {
            Text("Save", color = Color.White, fontSize = 20.sp)
        }
        OutlinedButton(
            onClick = {
                showAlert.value = true
            },
            colors = ButtonDefaults.buttonColors(backgroundColor = Teal200),
            modifier = Modifier
                .fillMaxWidth()
                .padding(start = 40.dp, end = 50.dp, bottom = 30.dp)
        ) {
            Text("Delete", color = Color.White, fontSize = 20.sp)
        }
        if (showAlert.value) {
            AlertDialog(
                onDismissRequest = {showAlert.value = false},
                confirmButton = {
                    TextButton(onClick = {
                        repository.deleteFood(food.value)
                        updateWidgets(context)
                        navController.popBackStack()
                    })
                    { Text(text = "Yes", color=principalColor) }
                },
                dismissButton = {
                    TextButton(onClick = {
                        showAlert.value = false
                    })
                    { Text(text = "No", color= principalColor) }
                } ,
                title = { Text(text = "Are you sure you want to delete " + food.value.foodName.uppercase() + "?" ) })
        }
    }
}

enum class NutritionItem(val descriptor: String) {
    CALORIES("Calories"),
    SODIUM("Sodium"),
    SUGAR("Sugar"),
    FATS("Fats"),
    PROTEINS("Proteins");

    override fun toString(): String {
        return descriptor
    }

    companion object {
        fun getEnum(value: String): NutritionItem {
            return values().first { it.descriptor == value }
        }
    }
}

@Composable
fun Item(
    food: MutableState<Food>,
    nutritionItem: NutritionItem
){
    val initialValue = when (nutritionItem) {
        NutritionItem.CALORIES -> food.value.calories.toString()
        NutritionItem.SODIUM -> food.value.sodium.toString()
        NutritionItem.SUGAR -> food.value.sugar.toString()
        NutritionItem.FATS -> food.value.fats.toString()
        NutritionItem.PROTEINS -> food.value.protein.toString()
    }

    var inputValueG by remember { mutableStateOf(initialValue)}

    LaunchedEffect(initialValue) {
        inputValueG = initialValue
    }

    val outlineTextFieldColors = TextFieldDefaults.outlinedTextFieldColors(
        focusedBorderColor = principalColor, // change the border color when focused
        textColor = Color.Black, // change the text color
        focusedLabelColor = principalColor,
        unfocusedBorderColor = Color.Gray,
        disabledBorderColor = Color.Gray
    )
    val focusManager = LocalFocusManager.current
    OutlinedTextField(
        value = inputValueG,
        onValueChange = { newValue -> inputValueG = newValue },
        label = { Text("Set $nutritionItem", fontSize = 15.sp, textAlign = TextAlign.Center) },
        placeholder = { Text("") },
        modifier = Modifier
            .padding(start = 20.dp, end = 30.dp, bottom = 10.dp)
            .fillMaxWidth()
            .height(55.dp),
        keyboardOptions = KeyboardOptions.Default.copy(
            keyboardType = KeyboardType.Number,
            imeAction = ImeAction.Done
        ),
        keyboardActions = KeyboardActions(
            onDone = {
                focusManager.clearFocus()
                when (nutritionItem) {
                    NutritionItem.CALORIES -> food.value.calories = if (inputValueG == "") 0 else inputValueG.toInt()
                    NutritionItem.SODIUM -> food.value.sodium = if (inputValueG == "") 0 else inputValueG.toInt()
                    NutritionItem.SUGAR -> food.value.sugar = if (inputValueG == "") 0 else inputValueG.toInt()
                    NutritionItem.FATS -> food.value.fats = if (inputValueG == "") 0 else inputValueG.toInt()
                    NutritionItem.PROTEINS -> food.value.protein = if (inputValueG == "") 0 else inputValueG.toInt()
                }
            }
        ),
        textStyle = TextStyle(textAlign = TextAlign.End, fontSize = 15.sp),
        visualTransformation = if (nutritionItem != NutritionItem.CALORIES) {
            SuffixVisualTransformation(" g")
        } else {
            SuffixVisualTransformation("  ")
        },
        colors = outlineTextFieldColors,
        shape = RoundedCornerShape(percent = 10)
    )
}