package com.app.kekomi

import androidx.compose.foundation.Image
import androidx.compose.runtime.Composable
import androidx.compose.foundation.layout.Column
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.foundation.border
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

@Preview
@Composable
fun ProfileDetailsScreen() {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(start = 10.dp, top = 20.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
//        verticalArrangement = Arrangement.Center
    ) {
        MyImage()
        Text(text = "Profile Details", modifier = Modifier.padding(top=20.dp))
        TextBox("Username")
        TextBox("Email")
        pesoYAltura()
        // add profile view and functionality here
        CheckBoxes()

    }
}

@Composable
fun pesoYAltura() {
    Row(modifier = Modifier.fillMaxWidth()) {
        OutlinedTextField(
            value = "",
            onValueChange = { },
            label = { Text("Peso") },
            modifier = Modifier.weight(1f).padding(8.dp)
        )
        OutlinedTextField(
            value = "",
            onValueChange = { },
            label = { Text("Altura") },
            modifier = Modifier.weight(1f).padding(8.dp)
        )
    } 
}

@Composable
fun TextBox(s: String) {
    OutlinedTextField(
        value = "",
        onValueChange = { },
        label = { Text(text = s) },
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 16.dp, horizontal = 32.dp)
    )
}

@Composable
fun MyImage() {
    val image = painterResource(R.drawable.ic_mati)
    Image(
        painter = image,
        contentDescription = "hola soy mati",
        modifier = Modifier
            .size(250.dp)
            .clip(CircleShape)
            .border(5.dp, Color.Gray, CircleShape)
    )
}

@Composable
fun CheckBoxes() {
    val items = listOf("Opción 1", "Opción 2", "Opción 3", "Opción 4", "Opción 5", "Opción 6")
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
                        Checkbox(
                            checked = checkedItems[index],
                            onCheckedChange = { isChecked ->
                                checkedItems[index] = isChecked
                            },
                            modifier = Modifier.padding(end = 16.dp)
                        )
                        Text(text = item,modifier = Modifier.padding(vertical = 8.dp))
                    }
                }
            }
            Column {
                items.takeLast(items.size / 2).forEachIndexed { index, item ->
                    Row(modifier = Modifier.padding(vertical = 8.dp)) {
                        Checkbox(
                            checked = checkedItems[index + items.size / 2],
                            onCheckedChange = { isChecked ->
                                checkedItems[index + items.size / 2] = isChecked
                            },
                            modifier = Modifier.padding(end = 16.dp)
                        )
                        Text(text = item,modifier = Modifier.padding(vertical = 8.dp))
                    }
                }
            }
        }
    }
}
