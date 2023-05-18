package com.app.kekomi.BottomNav

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.app.kekomi.Views.*

@Composable
fun NavigationGraph(navController: NavHostController) {
    NavHost(navController, startDestination = BottomNavItem.Home.screen_route) {
        composable(BottomNavItem.Home.screen_route) {
            HomeView(navController)
        }
        composable(BottomNavItem.Calendar.screen_route) {
            CalendarView()
        }
        composable(BottomNavItem.Profile.screen_route) {
            ProfileView()
        }
        composable("AddFoodView") {
            AddFoodView(navController)
        }
        composable("FoodDetailsView") {
            FoodDetailsView(navController)
        }
        composable("CodeBarScannerView") {
            CodeBarScannerView(navController)
        }
    }
}