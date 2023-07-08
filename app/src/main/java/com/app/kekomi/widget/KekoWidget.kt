package com.app.kekomi.widget

import android.appwidget.AppWidgetManager
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.util.Log
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.height
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.glance.*
import androidx.glance.action.ActionParameters
import androidx.glance.action.actionStartActivity
import androidx.glance.action.clickable
import androidx.glance.appwidget.GlanceAppWidget
import androidx.glance.appwidget.GlanceAppWidgetManager
import androidx.glance.appwidget.GlanceAppWidgetReceiver
import androidx.glance.appwidget.action.ActionCallback
import androidx.glance.appwidget.action.actionRunCallback
import androidx.glance.appwidget.action.actionStartActivity
import androidx.glance.appwidget.state.updateAppWidgetState
import androidx.glance.layout.*
import androidx.glance.state.PreferencesGlanceStateDefinition
import androidx.glance.text.*
import androidx.glance.unit.ColorProvider
import androidx.navigation.Navigation
import androidx.navigation.Navigation.findNavController
import com.app.kekomi.MainActivity
import com.app.kekomi.entities.Food
import com.app.kekomi.storage.FoodRepository
import com.app.kekomi.ui.theme.principalColor
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.launch
import java.time.LocalDate

class KekoMiWidget : GlanceAppWidget() {
 @Preview
 @Composable
 override fun Content() {
  Widget()
 }
}

@Composable
fun Widget() {

 val pref = currentState<Preferences>()
 val calories = pref[stringPreferencesKey("calories")]
 val sodium = pref[stringPreferencesKey("sodium")]
 val sugar = pref[stringPreferencesKey("sugar")]
 Column(
  modifier = GlanceModifier
   .fillMaxSize()
   .background(principalColor)
   .padding(2.dp),
  verticalAlignment = Alignment.Vertical.CenterVertically,
  horizontalAlignment = Alignment.Horizontal.CenterHorizontally
 ) {
   Button(
    text = "KekoMi",
    style = TextStyle(
     fontWeight = FontWeight.Bold,
     fontStyle = FontStyle.Italic,
     fontSize = 20.sp
     ),
    colors = ButtonColors(ColorProvider(Color.Transparent), ColorProvider(Color.White)),
    onClick = actionStartActivity<MainActivity>()
   )
  Text(
    modifier = GlanceModifier.padding(1.dp),
    text = "Calories " + calories + " kcal",
    style = TextStyle(
     color = ColorProvider(Color.White),
     fontWeight = FontWeight.Bold,
     fontSize = 14.sp
    )
   )
   Text(
    modifier = GlanceModifier.padding(3.dp),
    text = "Sodium " + sodium + " g",
    style = TextStyle(
     color = ColorProvider(Color.White),
     fontWeight = FontWeight.Bold,
     fontSize = 14.sp
    )
   )
   Text(
    modifier = GlanceModifier.padding(3.dp),
    text = "Sugar " + sugar + " g",
    style = TextStyle(
     color = ColorProvider(Color.White),
     fontWeight = FontWeight.Bold,
     fontSize = 14.sp
    )
   )
  Spacer(modifier = GlanceModifier.height(10.dp))
  Button(
    text = "Add Food",
    style = TextStyle(
     fontSize = 14.sp
    ),
    colors = ButtonColors(ColorProvider(Color.White), ColorProvider(principalColor)),
    onClick = actionStartActivity<MainActivity>()
   )
  }
}

class KekoMiWidgetReceiver : GlanceAppWidgetReceiver() {

 override val glanceAppWidget: GlanceAppWidget = KekoMiWidget()

 private val coroutineScope = MainScope()
 override fun onUpdate(
  context: Context,
  appWidgetManager: AppWidgetManager,
  appWidgetIds: IntArray
 ) {
  super.onUpdate(context, appWidgetManager, appWidgetIds)
  observeData(context)
 }


 private fun observeData(context: Context) {
  val repository = FoodRepository(context)
  val stats = repository.getStatsFrom(LocalDate.now())

  coroutineScope.launch {

   val glanceId =
    GlanceAppWidgetManager(context).getGlanceIds(KekoMiWidget::class.java).firstOrNull()

   glanceId?.let {
    updateAppWidgetState(context, PreferencesGlanceStateDefinition, it) { pref ->
     pref.toMutablePreferences().apply {
      this[calories] = stats?.calories.toString().replace("null","0")
      this[sodium] = stats?.sodium.toString().replace("null","0")
      this[sugar] = stats?.sugar.toString().replace("null","0")
     }
    }
     glanceAppWidget.update(context, it)
    }
  }
 }
  companion object {
   val calories = stringPreferencesKey("calories")
   val sodium = stringPreferencesKey("sodium")
   val sugar = stringPreferencesKey("sugar")
  }
}