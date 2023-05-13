package com.app.kekomi.Views

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.Icon
import androidx.compose.material.TextButton
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.app.kekomi.entities.Food
import com.app.kekomi.entities.Meal
import com.app.kekomi.storage.FoodRepository
import java.util.*

@Composable
fun AddFoodView(navController: NavHostController) {

    val context = LocalContext.current
    val repo:FoodRepository by lazy {
        FoodRepository(context)
    }

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
                Icon(Icons.Default.Close, contentDescription = "Localized description", tint = Color.Black)
            }
        }

        TextButton(
            onClick = {
                repo.insertFood(Food(foodName = "hamburguesa",
                    calories = 1,
                    sodium = 2,
                    sugar = 3,
                    fats = 4,
                    protein = 5,
                    date = Date(),
                    quantity = 6,
                    meal = Meal.BREAKFAST))
            },
            colors = ButtonDefaults.buttonColors(backgroundColor = Color.Transparent),
        ) {
            Icon(Icons.Default.Close, contentDescription = "Localized description", tint = Color.Black)
        }

    }
}