package com.app.kekomi.Views

import android.annotation.SuppressLint
import android.content.Context
import android.widget.Toast
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
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.*
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.app.kekomi.R
import com.app.kekomi.storage.userPreferences
import com.app.kekomi.ui.theme.principalColor
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

@Preview
@Composable
fun ProfileView() {
    Column{
        TopAppBar(
            backgroundColor = principalColor,
            modifier = Modifier
                .fillMaxWidth()
                .wrapContentHeight(),
            title = {
                Text(
                    text = "Settings",
                    textAlign = TextAlign.Center,
                    fontSize = 26.sp,
                    color = Color.White,
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

            Text(text = "Select what you wish to track:",
                modifier = Modifier.padding(top=20.dp),
                fontSize = 26.sp,
                color = principalColor)
            CheckBoxes()

        }
    }

}

@Composable
fun InformacionPersonal() {
    // context
    val context = LocalContext.current

    //scope
    val scope = rememberCoroutineScope()

    // datastore
    val dataStore = userPreferences(context)
    TextBox("Name", dataStore, scope)
    Spacer(modifier = Modifier.height(10.dp))
    PesoYAltura(dataStore, scope)
}

@Composable
fun PesoYAltura(dataStore: userPreferences, scope: CoroutineScope) {
    val focusManager = LocalFocusManager.current

    val initialValueH = dataStore.getHeight.collectAsState(initial = "").value!!
    var inputValueH by remember { mutableStateOf(initialValueH) }

    LaunchedEffect(initialValueH) {
        inputValueH = initialValueH
    }

    val initialValueW = dataStore.getWeight.collectAsState(initial = "").value!!
    var inputValueW by remember { mutableStateOf(initialValueW) }

    LaunchedEffect(initialValueW) {
        inputValueW = initialValueW
    }

    val outlineTextFieldColors = TextFieldDefaults.outlinedTextFieldColors(
        focusedBorderColor = principalColor, // change the border color when focused
        textColor = Color.Black, // change the text color
        focusedLabelColor = principalColor,
        unfocusedBorderColor = Color.Gray,
        disabledBorderColor = Color.Gray
    )

    Row(modifier = Modifier.fillMaxWidth()) {
        OutlinedTextField(
            value = inputValueW,
            onValueChange = { newValue ->
                // Filter out non-digit characters
                val filteredValue = newValue.filter { it.isDigit() }
                inputValueW = filteredValue
            },
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
                    scope.launch {
                        dataStore.saveWeight(inputValueW)
                    }
                }
            ),
            textStyle = TextStyle(textAlign = TextAlign.End, fontSize = 20.sp),
            visualTransformation = SuffixVisualTransformation(" kg"),
            colors = outlineTextFieldColors
        )

        OutlinedTextField(
            value = inputValueH,
            onValueChange = { newValue ->
                // Filter out non-digit characters
                val filteredValue = newValue.filter { it.isDigit() }
                inputValueH = filteredValue
            },
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
                    scope.launch {
                        dataStore.saveHeight(inputValueH)
                    }
                }
            ),
            textStyle = TextStyle(textAlign = TextAlign.End, fontSize = 20.sp),
            visualTransformation = SuffixVisualTransformation(" cm"),
            colors = outlineTextFieldColors
        )
    }
}




@Composable
fun TextBox(s: String, dataStore: userPreferences, scope: CoroutineScope){
    val initialValue = dataStore.getName.collectAsState(initial = "").value!!

    var inputValue by remember { mutableStateOf(initialValue) }

    LaunchedEffect(initialValue) {
        inputValue = initialValue
    }
    val focusManager = LocalFocusManager.current
    val outlineTextFieldColors = TextFieldDefaults.outlinedTextFieldColors(
        focusedBorderColor = principalColor, // change the border color when focused
        textColor = Color.Black, // change the text color
        focusedLabelColor = principalColor,
        unfocusedBorderColor = Color.Gray,
        disabledBorderColor = Color.Gray,

        )

    OutlinedTextField(
        textStyle = TextStyle(fontSize = 20.sp),
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
                scope.launch {
                    dataStore.saveName(inputValue)
                }
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

@SuppressLint("UnrememberedMutableState", "CoroutineCreationDuringComposition")
@Composable
fun CheckBoxes() {
//    val items = listOf("Calories", "Sodium", "Sugar", "Fats", "Protein")

    var checkedItems = mutableStateListOf<Boolean>()

    // context
    val context = LocalContext.current

    //scope
    val scope = rememberCoroutineScope()

    // datastore
    val dataStore = userPreferences(context)

    // get states

    val states = dataStore.getStates.map { item -> item.collectAsState(initial = "").value!!}

    states.forEach{item -> checkedItems.add(item.toString().toBoolean())}

    //Get Items

    val items = dataStore.getItems()

    // El primero es Calories y lo seteo en true
    checkedItems[0] = true

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
                            scope.launch {//se necesita para guardar cosas, es horrible
                                dataStore.saveStates(checkedItems)
                            }
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

//                Toast.makeText(context, goalsValues[index], Toast.LENGTH_SHORT).show()
                Goal(item,  checkedItems[index], index, dataStore, scope)

            }

        }
    }
}

suspend fun checkUpdateGoals(
    updatedList: MutableList<String>,
    goalsValues: MutableList<String>,
    dataStore: userPreferences,
    context: Context
) {

    if (!updatedList.equals(goalsValues)) {
        Toast.makeText(context, updatedList.joinToString(" ,d "), Toast.LENGTH_SHORT).show()
        dataStore.saveGoals(updatedList as SnapshotStateList<String>)
    }
}




@Composable
fun Goal(
    item: String,
    isChecked: Boolean,
    index: Int,
    dataStore: userPreferences,
    scope: CoroutineScope
){

    val initialValue = dataStore.getGoal(index).collectAsState(initial = "").value!!

    var inputValueG by remember { mutableStateOf(initialValue) }

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
        onValueChange = { newValue ->
            // Filter out non-digit characters
            val filteredValue = newValue.filter { it.isDigit() }
            inputValueG = filteredValue
        },
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
                updateGoal(inputValueG, index,dataStore, scope)
            }
        ),
        textStyle = TextStyle(textAlign = TextAlign.End, fontSize = 15.sp),
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

@SuppressLint("CoroutineCreationDuringComposition")
fun updateGoal(value: String, index: Int, dataStore: userPreferences, scope: CoroutineScope) {
    scope.launch {
        dataStore.saveGoal(value, index)
    }
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

