package com.app.kekomi.Views

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
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.ArrowForward
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.OffsetMapping
import androidx.compose.ui.text.input.TransformedText
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.app.kekomi.Extras.DatePicker
import com.app.kekomi.Extras.decrementDay
import com.app.kekomi.Extras.incrementDate
import com.app.kekomi.Extras.showDate
import com.app.kekomi.R

@Preview
@Composable
fun ProfileView() {

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(bottom = 50.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
//        verticalArrangement = Arrangement.Center
    ) {
        TopAppBar(
            modifier = Modifier.padding(bottom =20.dp),
            backgroundColor = Color(android.graphics.Color.parseColor("#008080")),
            title = {
                Row(
                    modifier = Modifier.weight(1f),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.Center
                ) {

                    Text(text = "Profile Details", fontSize = 26.sp, color = Color.White)

                }
            }
        )
        MyImage()
        TextBox("Name")
        PesoYAltura()
        Text(text = "Select what you wish to track:", modifier = Modifier.padding(top=20.dp), fontSize = 26.sp, color = Color(android.graphics.Color.parseColor("#008080")))
        CheckBoxes()

    }
}

@Composable
fun PesoYAltura() {
    var inputValueW by remember {
        mutableStateOf("")
    }

    var inputValueH by remember {
        mutableStateOf("")
    }

    Row(modifier = Modifier.fillMaxWidth()) {
        OutlinedTextField(
            value = inputValueW,
            onValueChange = { newValue -> inputValueW = newValue},
            label = { Text("Weight", fontSize = 18.sp) },
            modifier = Modifier
                .weight(1f)
                .padding(8.dp)
                .background(Color.White),
            keyboardOptions = KeyboardOptions.Default.copy(
                keyboardType = KeyboardType.Number
            ),

        )
        OutlinedTextField(
            value = inputValueH,
            onValueChange = {newValue -> inputValueH = newValue},
            label = { Text("Height", fontSize = 18.sp) },
            modifier = Modifier
                .weight(1f)
                .padding(8.dp)
                .background(Color.White),
            keyboardOptions = KeyboardOptions.Default.copy(
                keyboardType = KeyboardType.Number
            ),
        )
    }
}

@Composable
fun TextBox(s: String){
    var inputValue by remember {
        mutableStateOf("")
    }

    OutlinedTextField(
        value = inputValue,
        onValueChange = {newValue -> inputValue = newValue},
        label = { Text(text = s,fontSize = 18.sp) },
        placeholder = { Text(s) },
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
            .size(120.dp)
            //.clip(CircleShape)
            //.border(5.dp, Color.Black, CircleShape)
            .padding(10.dp)
    )
}

@Composable
fun CheckBoxes() {
    val items = listOf("Calories", "Sodium", "Sugar", "Fats", "Protein")
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
                    .padding(vertical = 8.dp)
                    .fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
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
                    fontSize = 15.sp,
                    modifier = Modifier.padding(start = 8.dp)
                )

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

    )

    TextField(
        value = inputValueG,
        onValueChange = { newValue -> inputValueG = newValue },
        label = { Text( "Set goal", fontSize = 15.sp, textAlign = TextAlign.Center) },
        placeholder = { Text("") },
        modifier = Modifier
            .padding(1.dp)
            .background(Color.White)
            .width(100.dp),
        keyboardOptions = KeyboardOptions.Default.copy(
            keyboardType = KeyboardType.Number
        ),
        textStyle = TextStyle(textAlign = TextAlign.End),
        enabled = isChecked,
        readOnly = !isChecked,
        visualTransformation = SuffixVisualTransformation(" g"),
        colors = outlineTextFieldColors


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


