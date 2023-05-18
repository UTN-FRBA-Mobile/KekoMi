package com.app.kekomi

import android.annotation.SuppressLint
import android.content.Context
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.material.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.preferencesDataStore
import androidx.navigation.compose.rememberNavController
import com.app.kekomi.BottomNav.BottomNavigation
import com.app.kekomi.ui.theme.KekoMiTheme
import com.app.kekomi.BottomNav.NavigationGraph
import com.app.kekomi.storage.userPreferences


class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //setContent {
          //  KekoMiTheme {
                // A surface container using the 'background' color from the theme
                setContent {
                    App()
                }

            //}
        //}
    }
}

@SuppressLint("UnusedMaterialScaffoldPaddingParameter")
@Preview
@Composable
private fun App() {
    val navController = rememberNavController()
    Scaffold(
        bottomBar = { BottomNavigation(navController = navController) }
    ) {
        NavigationGraph(navController = navController)
    }

}

@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
    KekoMiTheme {

    }
}