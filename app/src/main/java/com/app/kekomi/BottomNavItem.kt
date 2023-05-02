package com.app.kekomi

sealed class BottomNavItem(var title:String, var icon:Int, var screen_route:String){

    object Home : BottomNavItem("Home", R.drawable.ic_home,"home")
    object Calendar: BottomNavItem("Calendar", R.drawable.ic_calendar,"calendar")
    object Profile: BottomNavItem("Profile", R.drawable.ic_profile,"profile")
}