package com.app.kekomi.widget

import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.glance.GlanceModifier
import androidx.glance.appwidget.GlanceAppWidget
import androidx.glance.appwidget.GlanceAppWidgetReceiver
import androidx.glance.background
import androidx.glance.layout.Alignment
import androidx.glance.layout.Column
import androidx.glance.layout.Row
import androidx.glance.layout.fillMaxSize


class KekoMiWidget : GlanceAppWidget() {
 @Composable
 override fun Content() {
 }
}

class KekoMiWidgetReceiver : GlanceAppWidgetReceiver() {
 override val glanceAppWidget: GlanceAppWidget = KekoMiWidget()
}