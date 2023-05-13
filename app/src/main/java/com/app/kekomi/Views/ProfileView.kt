package com.app.kekomi.Views

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.runtime.Composable
import androidx.compose.foundation.layout.Column
import androidx.compose.material.Text
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.app.kekomi.R

@Preview
@Composable
fun ProfileView() {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(start = 10.dp, bottom = 50.dp, top = 30.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
//        verticalArrangement = Arrangement.Center
    ) {
        Text(text = "Profile Details", modifier = Modifier.padding(bottom =10.dp),fontSize = 26.sp, color = Color(android.graphics.Color.parseColor("#008080")))
        MyImage()
        TextBox("Name")
       // TextBox("Email")
        PesoYAltura()
        Text(text = "Select what you wish to track:", modifier = Modifier.padding(top=20.dp), fontSize = 26.sp, color = Color(android.graphics.Color.parseColor("#008080")))
        CheckBoxes()

    }
}

@Composable
fun PesoYAltura() {
    Row(modifier = Modifier.fillMaxWidth()) {
        OutlinedTextField(
            value = "",
            onValueChange = { },
            label = { Text("Weight", fontSize = 18.sp) },
            modifier = Modifier.weight(1f).padding(8.dp).background(Color.White)

        )
        OutlinedTextField(
            value = "",
            onValueChange = { },
            label = { Text("Height", fontSize = 18.sp) },
            modifier = Modifier.weight(1f).padding(8.dp).background(Color.White)
        )
    } 
}

@Composable
fun TextBox(s: String) {

    OutlinedTextField(
        value = "",
        onValueChange = { },
        label = { Text(text = s,fontSize = 18.sp) },
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 16.dp, horizontal = 32.dp)
            .background(Color.White)

    )
}

@Composable
fun MyImage() {
    val image = painterResource(R.drawable.ic_profile2)
    Image(
        painter = image,
        contentDescription = "hola soy mati",
        modifier = Modifier
            .size(200.dp)
            //.clip(CircleShape)
            //.border(5.dp, Color.Black, CircleShape)
            .padding(10.dp)
    )
}

@Composable
fun CheckBoxes() {
    val items = listOf("Calories", "Sodium", "Sugar", "Fats", "Protein", "Opción 6")
    val checkedItems = remember { mutableStateListOf<Boolean>() }

    // Initialize the list of selected items with false values
    if (checkedItems.isEmpty()) {
        repeat(items.size) {
            checkedItems.add(false)
        }
    }

    Column(modifier = Modifier.padding(16.dp)) {
        Row {
            Column {
                items.take(items.size / 2).forEachIndexed { index, item ->
                    Row(modifier = Modifier.padding(vertical = 8.dp)) {
                        Box(
                            modifier = Modifier
                                .background(Color.White)
                                .clip(shape = RoundedCornerShape(4.dp))
                                .size(18.dp)
                        ) {
                            Checkbox(
                                checked = checkedItems[index],
                                onCheckedChange = { isChecked ->
                                    checkedItems[index] = isChecked
                                },
                                //modifier = Modifier.padding(end = 8.dp)
                            )
                        }
                        Text(
                            text = item,
                            color = Color.Black,
                            fontSize = 20.sp,
                            modifier = Modifier.padding(start = 8.dp)
                        )
                    }
                }
            }
            Spacer(Modifier.width(40.dp))
            Column {
                items.takeLast(items.size / 2).forEachIndexed { index, item ->
                    Row(modifier = Modifier.padding(vertical = 8.dp)) {
                        Box(
                            modifier = Modifier
                                .background(Color.White)
                                .clip(shape = RoundedCornerShape(4.dp))
                                .size(18.dp)
                        ) {
                            Checkbox(
                                checked = checkedItems[index + items.size / 2],
                                onCheckedChange = { isChecked ->
                                    checkedItems[index + items.size / 2] = isChecked
                                },
                               // modifier = Modifier.padding(end = 8.dp)
                            )
                        }
                        Text(
                            text = item,
                            color = Color.Black,
                            fontSize = 20.sp,
                            modifier = Modifier.padding(start = 8.dp)
                        )
                    }
                }
            }
        }
    }
}