package com.app.kekomi.BottomNav

import com.app.kekomi.R

sealed class BottomNavItem(var title:String, var icon:Int, var screen_route:String){

    object Home : BottomNavItem("Home", R.drawable.ic_home,"home")
    object Calendar: BottomNavItem("Stats", R.drawable.ic_stacked_bar_chart,"calendar")
    object Profile: BottomNavItem("Settings", R.drawable.ic_settings ,"profile")
}