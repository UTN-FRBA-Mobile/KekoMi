package com.app.kekomi.Views

import android.widget.Space
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.material.Text
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.*
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.app.kekomi.R

@Preview
@Composable
fun ProfileView() {
    Column{
        TopAppBar(
            backgroundColor = Color(android.graphics.Color.parseColor("#008080")),
            modifier = Modifier
                .fillMaxWidth()
                .wrapContentHeight(),
            title = {
                Text(
                    text = "Settings",
                    textAlign = TextAlign.Center,
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(1f)
                )
            }
        )
        Column(
            modifier = Modifier
                .verticalScroll(rememberScrollState())
                .padding(bottom = 50.dp)
                .fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
//            verticalArrangement = Arrangement.Center
        ) {

            MyImage()

            InformacionPersonal()

            Text(text = "Select what you wish to track:", modifier = Modifier.padding(top=20.dp), fontSize = 26.sp, color = Color(android.graphics.Color.parseColor("#008080")))
            CheckBoxes()

        }
    }

}

@Composable
fun InformacionPersonal() {
    TextBox("Name")
    Spacer(modifier = Modifier.height(10.dp))
    PesoYAltura()
}

@Composable
fun PesoYAltura() {
    var inputValueW by remember {
        mutableStateOf("")
    }

    var inputValueH by remember {
        mutableStateOf("")
    }
    val focusManager = LocalFocusManager.current

    val outlineTextFieldColors = TextFieldDefaults.outlinedTextFieldColors(
        focusedBorderColor = Color(0xFF008080), // change the border color when focused
        textColor = Color.Black, // change the text color
        focusedLabelColor = Color(0xFF008080),
        unfocusedBorderColor = Color.Gray,
        disabledBorderColor = Color.Gray,

        )

    Row(modifier = Modifier.fillMaxWidth()) {
        OutlinedTextField(
            value = inputValueW,
            onValueChange = { newValue -> inputValueW = newValue},
            label = { Text("Weight", fontSize = 18.sp) },
            modifier = Modifier
                .weight(1f)
                .padding(start = 20.dp, end = 10.dp)
                .background(Color.White),
            keyboardOptions = KeyboardOptions.Default.copy(
                keyboardType = KeyboardType.Number,
                imeAction = ImeAction.Done
            ),
            keyboardActions = KeyboardActions(
                onDone = {
                    focusManager.clearFocus()
                }
            ),
            textStyle = TextStyle(textAlign = TextAlign.End),
            visualTransformation = SuffixVisualTransformation(" kg"),
            colors = outlineTextFieldColors


        )
        OutlinedTextField(
            value = inputValueH,
            onValueChange = {newValue -> inputValueH = newValue},
            label = { Text("Height", fontSize = 18.sp) },
            modifier = Modifier
                .weight(1f)
                .padding(start = 10.dp, end = 20.dp)
                .background(Color.White),
            keyboardOptions = KeyboardOptions.Default.copy(
                keyboardType = KeyboardType.Number,
                imeAction = ImeAction.Done
            ),
            keyboardActions = KeyboardActions(
                onDone = {
                    focusManager.clearFocus()
                }
            ),
            textStyle = TextStyle(textAlign = TextAlign.End),
            visualTransformation = SuffixVisualTransformation(" cm"),
            colors = outlineTextFieldColors
        )
    }
}

@Composable
fun TextBox(s: String){
    var inputValue by remember {
        mutableStateOf("")
    }
    val focusManager = LocalFocusManager.current
    val outlineTextFieldColors = TextFieldDefaults.outlinedTextFieldColors(
        focusedBorderColor = Color(0xFF008080), // change the border color when focused
        textColor = Color.Black, // change the text color
        focusedLabelColor = Color(0xFF008080),
        unfocusedBorderColor = Color.Gray,
        disabledBorderColor = Color.Gray,

    )

    OutlinedTextField(
        value = inputValue,
        onValueChange = {newValue ->
            inputValue = newValue},
        label = { Text(text = s,fontSize = 18.sp) },
        placeholder = { Text(s) },
        modifier = Modifier
            .fillMaxWidth()
            .padding(start = 20.dp, top = 16.dp, end = 20.dp)
            .background(Color.White),
        keyboardOptions = KeyboardOptions(
            imeAction = ImeAction.Done // Set the keyboard button to "Done"
        ),
        keyboardActions = KeyboardActions(
            onDone = {
                focusManager.clearFocus()
            }
        ),
        visualTransformation = SuffixVisualTransformation("  "),
        colors = outlineTextFieldColors

    )

}

@Composable
fun MyImage() {
    val image = painterResource(R.drawable.ic_profile2)
    Image(
        painter = image,
        contentDescription = "hola soy mati",
        modifier = Modifier
            .size(120.dp)
            //.clip(CircleShape)
            //.border(5.dp, Color.Black, CircleShape)
            .padding(top = 20.dp)
    )
}

@Composable
fun CheckBoxes() {
    val items = listOf("Calories", "Sodium", "Sugar", "Fats", "Protein", "Opción 6","dea")
    var checkedItems = remember { mutableStateListOf<Boolean>() }

    // Initialize the list of selected items with false values
    if (checkedItems.isEmpty()) {
        repeat(items.size) {
            if (items[it] == "Calories") {
                checkedItems.add(true)
            } else {
                checkedItems.add(false)
            }
        }
    }

    Column(modifier = Modifier.padding(10.dp)) {
        items.take(items.size).forEachIndexed { index, item ->
            Row(
                modifier = Modifier
                    .padding(vertical = 8.dp, horizontal = 10.dp)
                    .fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Start
            ) {
                Checkbox(
                    checked = checkedItems[index],
                    onCheckedChange = { isChecked ->
                        if(items[index] != "Calories"){
                            checkedItems[index] = isChecked
                        }

                    },
                    //modifier = Modifier.padding(end = 8.dp)
                )

                Text(
                    text = item,
                    color = Color.Black,
                    fontSize = 20.sp,
                    modifier = Modifier.padding(start = 10.dp, end = 80.dp),
                    textAlign = TextAlign.Left
                )
                Spacer(modifier = Modifier.weight(1f))

                goal(item,  checkedItems[index])
            }

        }
    }
}

@Composable
fun goal(item: String, isChecked: Boolean) {
    var inputValueG by remember { mutableStateOf("") }
    val outlineTextFieldColors = TextFieldDefaults.outlinedTextFieldColors(
        focusedBorderColor = Color(0xFF008080), // change the border color when focused
        textColor = Color.Black, // change the text color
        focusedLabelColor = Color(0xFF008080),
        unfocusedBorderColor = Color.Gray,
        disabledBorderColor = Color.Gray,
//        backgroundColor = Color(red = 209, green = 209, blue = 209)
    )
    val focusManager = LocalFocusManager.current
    OutlinedTextField(
        value = inputValueG,
        onValueChange = { newValue -> inputValueG = newValue },
        label = { Text( "Set goal", fontSize = 15.sp, textAlign = TextAlign.Center) },
        placeholder = { Text("") },
        modifier = Modifier
            .padding(end = 10.dp)
            .width(100.dp)
            .height(50.dp),
        keyboardOptions = KeyboardOptions.Default.copy(
            keyboardType = KeyboardType.Number,
            imeAction = ImeAction.Done
        ),
        keyboardActions = KeyboardActions(
            onDone = {
                focusManager.clearFocus()
            }
        ),
        textStyle = TextStyle(textAlign = TextAlign.End),
        enabled = isChecked,
        readOnly = !isChecked,
        visualTransformation = if (item != "Calories") {
            SuffixVisualTransformation(" g")
        } else {
            SuffixVisualTransformation("  ")
        },
        colors = outlineTextFieldColors,
        shape = RoundedCornerShape(percent = 10)


    )
}


class SuffixVisualTransformation(private val suffix: String) : VisualTransformation {
    override fun filter(text: AnnotatedString): TransformedText {
        val transformedText = text + AnnotatedString(suffix, SpanStyle(Color.Gray))

        return TransformedText(transformedText, SuffixOffsetMapping(text.text))
    }
}

class SuffixOffsetMapping(private val originalText: String) : OffsetMapping {
    override fun originalToTransformed(offset: Int): Int = offset

    override fun transformedToOriginal(offset: Int): Int {
        return if (offset > originalText.length) originalText.length else offset
    }
}


