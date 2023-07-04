package com.app.kekomi.widget

import android.appwidget.AppWidgetManager
import android.content.Context
import android.content.Intent
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.glance.*
import androidx.glance.action.ActionParameters
import androidx.glance.text.Text
import androidx.glance.appwidget.GlanceAppWidget
import androidx.glance.appwidget.GlanceAppWidgetManager
import androidx.glance.appwidget.GlanceAppWidgetReceiver
import androidx.glance.appwidget.action.ActionCallback
import androidx.glance.appwidget.action.actionRunCallback
import androidx.glance.appwidget.state.updateAppWidgetState
import androidx.glance.layout.*
import androidx.glance.state.PreferencesGlanceStateDefinition
import androidx.glance.text.TextAlign
import androidx.glance.text.TextStyle
import androidx.glance.unit.ColorProvider
import com.app.kekomi.entities.Food
import com.app.kekomi.storage.FoodRepository
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.launch
import java.time.LocalDate

class KekoMiWidget : GlanceAppWidget() {
 @Composable
 override fun Content() {
  Widget()
 }
}

@Composable
fun Widget(){

 val pref = currentState<Preferences>()
 val calories = pref[stringPreferencesKey("calories")]
 val sodium = pref[stringPreferencesKey("sodium")]
 val sugar = pref[stringPreferencesKey("sugar")]

 Column(
   horizontalAlignment = Alignment.CenterHorizontally,
   verticalAlignment = Alignment.CenterVertically,
   modifier = GlanceModifier.background(Color.White)
  ){
   Text(
    text = "Calories " + calories + " kcal",
    modifier = GlanceModifier.fillMaxWidth(),
    style = TextStyle(
     textAlign = TextAlign.Center,
     color = ColorProvider(MaterialTheme.colors.onSurface),
    )
   )
   Spacer(modifier = GlanceModifier.padding(8.dp))
   Text(
    text = "Sodium " +  sodium + " g",
    modifier = GlanceModifier.fillMaxWidth(),
    style = TextStyle(
     textAlign = TextAlign.Center,
     color = ColorProvider(MaterialTheme.colors.onSurface),
    )
   )
   Spacer(modifier = GlanceModifier.padding(8.dp))
   Text(
    text = "Sugar " + sugar + " g",
    modifier = GlanceModifier.fillMaxWidth(),
    style = TextStyle(
     textAlign = TextAlign.Center,
     color = ColorProvider(MaterialTheme.colors.onSurface),
    )
   )
   Button(
    text = "Add Food +",
    modifier = GlanceModifier.fillMaxWidth(),
    onClick = actionRunCallback<UpdateActionCallback>()
   )
  }
}


class UpdateActionCallback : ActionCallback {
 override suspend fun onAction(context: Context, glanceId: GlanceId, parameters: ActionParameters) {

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
      this[calories] = stats.calories.toString()
      this[sodium] = stats.sodium.toString()
      this[sugar] = stats.sugar.toString()
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