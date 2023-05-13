package com.app.kekomi.Views


import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CutCornerShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.MaterialTheme
import androidx.compose.material.OutlinedButton
import androidx.compose.material.Text
import androidx.compose.material.TopAppBar
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowForward
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.zIndex
import com.app.kekomi.Extras.DatePicker2
import com.app.kekomi.Extras.DonutChart
import com.app.kekomi.Extras.SelectableCalendar


@Preview
@Composable
fun CalendarView() {
    Column {
        TopAppBar(
            backgroundColor = Color(android.graphics.Color.parseColor("#008080")),
            modifier = Modifier.fillMaxWidth().wrapContentHeight(),
            title = {
                Text(
                    text = "Stats",
                    textAlign = TextAlign.Center,
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(1f)
                )
            }
        )

        ButtonToggleGroupCall()

    }
}

@Preview
@Composable
fun ButtonToggleGroupCall() {
    val semana = "Week"
    val mes = "Month"
    val seisMeses = "6Months"
    val anio = "Year"
    val options = listOf(semana, mes, seisMeses, anio)

    var selectedOption by remember { mutableStateOf(options[0]) }

    ButtonToggleGroup(
        options = options,
        selectedOption = selectedOption,
        onOptionSelect = { selectedOption = it
            when (it) {
                semana -> {
                    // función a ejecutar cuando se selecciona la opción 'Week'
                }
                mes -> {
                    // función a ejecutar cuando se selecciona la opción 'Month'
                }
                seisMeses -> {
                    // función a ejecutar cuando se selecciona la opción '6Months'
                }
                anio -> {
                    // función a ejecutar cuando se selecciona la opción 'Year'
                }
            } },
    )
}

@Composable
private fun ButtonToggleGroup(
    options: List<String>,
    selectedOption: String,
    onOptionSelect: (String) -> Unit,
    modifier: Modifier = Modifier
) {

    Row(modifier = modifier) {
        options.forEachIndexed { index, option ->
            val selected = selectedOption == option

            val border = if (selected) BorderStroke(
                width = 3.dp,
                color = Color(android.graphics.Color.parseColor("#008080"))
            ) else ButtonDefaults.outlinedBorder

            val shape = when (index) {
                0 -> RoundedCornerShape(
                    topStart = 4.dp,
                    bottomStart = 4.dp,
                    topEnd = 0.dp,
                    bottomEnd = 0.dp
                )
                options.size - 1 -> RoundedCornerShape(
                    topStart = 0.dp, bottomStart = 0.dp,
                    topEnd = 4.dp,
                    bottomEnd = 4.dp
                )
                else -> CutCornerShape(0.dp)
            }

            val zIndex = if (selected) 1f else 0f

            val buttonModifier = when (index) {
                0 -> Modifier.zIndex(zIndex)
                else -> {
                    val offset = -1 * index
                    Modifier
                        .offset(x = offset.dp)
                        .zIndex(zIndex)
                }
            }

            val colors = ButtonDefaults.outlinedButtonColors(
                backgroundColor = if (selected) MaterialTheme.colors.surface
                else MaterialTheme.colors.surface,
                contentColor = Color.DarkGray
            )

            OutlinedButton( // 9
                onClick = { onOptionSelect(option) },
                border = border,
                shape = shape,
                colors = colors,
                modifier = buttonModifier.weight(1f)
            ) {
                Text(option)
            }
        }
    }
}