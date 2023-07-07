package com.app.kekomi.BottomNav

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navDeepLink
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
        composable("AddFoodView/{scannedValue}") { backStackEntry ->
            val scannedValue = backStackEntry.arguments?.getString("scannedValue")
            AddFoodView(navController, scannedValue)
        }
        composable("FoodDetailsView/{foodId}") { navBackStackEntry ->
            val foodId = navBackStackEntry.arguments?.getString("foodId")
            if (foodId != null) {
                FoodDetailsView(navController, foodId.toInt())
            }
        }
        composable("CodeBarScannerView") {
            CodeBarScannerView(navController)
        }
    }
}