package com.app.kekomi.Extras

//import com.example.composedatetimepicker.ui.theme.ComposeDateTimePickerTheme
//import android.graphics.Color
import android.widget.Toast
import androidx.compose.foundation.layout.*
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import com.vanpra.composematerialdialogs.MaterialDialog
import com.vanpra.composematerialdialogs.datetime.date.DatePickerDefaults
import com.vanpra.composematerialdialogs.datetime.date.datepicker
import com.vanpra.composematerialdialogs.rememberMaterialDialogState
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.time.format.FormatStyle

object DateSelected {
    var pickedDate: LocalDate by mutableStateOf(LocalDate.now())
}
fun incrementDate() {
    DateSelected.pickedDate = DateSelected.pickedDate.plusDays(1)
}

fun decrementDay() {
    DateSelected.pickedDate = DateSelected.pickedDate.minusDays(1)
}
@Composable
fun showDate(){
    Text(
        text = DateSelected.pickedDate.format(DateTimeFormatter.ofPattern("MMM dd yyyy")),
        modifier = Modifier.padding(horizontal = 8.dp)
    )
}

@Composable
fun DatePicker() {
    val applicationContext = LocalContext.current

    val dateDialogState = rememberMaterialDialogState()

    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        IconButton(onClick = {
            dateDialogState.show()
        }) {
            Icon(
                imageVector = Icons.Filled.DateRange,
                contentDescription = "Select date",
            )
        }
        //Text(text = formattedDate)
    }

    MaterialDialog(
        dialogState = dateDialogState,
        buttons = {
            positiveButton(text = "Ok") {
                Toast.makeText(
                    applicationContext,
                    "Date updated",
                    Toast.LENGTH_LONG
                ).show()
            }
            negativeButton(text = "Cancel")
        }
    ) {
        datepicker(
            initialDate = DateSelected.pickedDate,
            title = "Pick a date",
            colors = DatePickerDefaults.colors(headerBackgroundColor = Color.Gray,dateActiveBackgroundColor = Color(android.graphics.Color.parseColor("#008080")))
        ) {
            DateSelected.pickedDate = it
        }
    }
}

