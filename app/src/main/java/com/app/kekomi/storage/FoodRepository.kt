package com.app.kekomi.storage
import android.content.Context
import com.app.kekomi.entities.Food
import com.app.kekomi.entities.Stats
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.launch
import java.time.LocalDate


class FoodRepository(context: Context) {

    var db: FoodDao = AppDatabase.getInstance(context)?.foodDao()!!

    //Fetch All the Food
    fun getAllFood(date: LocalDate): List<Food> {
        return db.getAllFood(date.dayOfMonth, date.monthValue, date.year)
    }

    // Insert new food
    fun insertFood(food: Food) {
        CoroutineScope(IO).launch {
            db.insertFood(food)
        }
    }

    // Update food
    fun updateFood(food: Food) {
        db.updateFood(food)
    }

    // Delete food
    fun deleteFood(food: Food) {
        db.deleteFood(food)
    }

    // Get stats from date
    fun getStatsFrom(date: LocalDate): Stats{
        return db.getStatsFrom(date.dayOfMonth, date.monthValue, date.year)
    }

    //Devuelve una lista ordenada del día más reciente al más viejo de la semana
    fun getWeekStats(): List<Stats>{
        return db.getWeekStats()
    }

    //Devuelve una lista ordenada de la semana más reciente a la más vieja del mes
    fun getMonthStats(): List<Stats>{
        return db.getMonthStats()
    }

    //Devuelve una lista ordenada del mes más reciente al mas viejo de los 6 meses
    fun get6MonthStats(): List<Stats>{
        return db.get6MonthStats()
    }

    //Devuelve una lista ordenada del mes más reciente al mas viejo del año
    fun getYearStats(): List<Stats>{
        return db.getYearStats()
    }
}