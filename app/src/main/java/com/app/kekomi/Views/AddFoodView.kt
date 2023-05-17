package com.app.kekomi.Views

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Search
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.app.kekomi.Extras.DateSelected
import com.app.kekomi.R
import com.app.kekomi.entities.Food
import com.app.kekomi.entities.Meal
import com.app.kekomi.storage.FoodRepository
import java.time.LocalDate
import java.time.ZoneId
import java.util.*
import kotlin.math.round

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

//        TextButton(
//            onClick = {
//                repo.insertFood(Food(foodName = "hamburguesa",
//                    calories = 1,
//                    sodium = 2,
//                    sugar = 3,
//                    fats = 4,
//                    protein = 5,
//                    date = Date.from(
//                        LocalDate.now().atStartOfDay(ZoneId.systemDefault())
//                        .toInstant()),
//                    quantity = 6,
//                    meal = Meal.BREAKFAST))
//            },
//            colors = ButtonDefaults.buttonColors(backgroundColor = Color.Transparent),
//        ) {
//            Icon(Icons.Default.Close, contentDescription = "Localized description", tint = Color.Black)
//        }


        PreviewSearchBar()

    }
}


@Composable
fun SearchBar(
    modifier: Modifier = Modifier,
    onSearch: (String) -> Unit,
    onClear: () -> Unit
) {
    var text by remember { mutableStateOf("") }
    val focusManager = LocalFocusManager.current

    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Start,
        modifier = Modifier.padding(start = 10.dp)
    ) {
            Row(
                modifier = modifier
                    .width(300.dp)
                    .padding(10.dp)
                    .border(
                        width = 2.dp,
                        color = Color.Gray,
                        shape = RoundedCornerShape(percent = 10)
                    ),
                verticalAlignment = Alignment.CenterVertically
            ) {


                Spacer(Modifier.width(2.dp))
                Icon(
                    imageVector = Icons.Filled.Search, contentDescription = "lupa",
                    modifier = Modifier.padding(5.dp)
                )
                BasicTextField(
                    value = text,
                    onValueChange = { newText ->
                        text = newText
                        onSearch(newText)
                    },
                    textStyle = TextStyle(color = Color.Black),
                    modifier = Modifier
                        .weight(1f)
                        .padding(end = 8.dp),
                    singleLine = true,
                    maxLines = 1,
                    keyboardOptions = KeyboardOptions(imeAction = ImeAction.Search),
                    keyboardActions = KeyboardActions(onSearch = {
                        focusManager.clearFocus()
                        onSearch(text)
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
                    .width(50.dp)
                    .border(
                        width = 2.dp,
                        color = Color.Gray,
                        shape = RoundedCornerShape(percent = 10)
                    )
                    .padding(end = 1.dp),
            ) {


                IconButton(onClick = {
                    text = ""
                    onClear()
                }) {
                    Icon(
                        painter = painterResource(id = R.drawable.codigo_de_barras),
                        contentDescription = "Clear",
                        tint = Color.Gray,
                        modifier = Modifier
                            .padding(start = 5.dp, end = 5.dp)
                            .size(50.dp)
                    )

                }
            }
        }
}




@Preview
@Composable
fun PreviewSearchBar() {
    SearchBar(onSearch = {}, onClear = {})
}


