package com.app.kekomi.storage
import android.content.Context
import com.app.kekomi.entities.Food
import com.app.kekomi.entities.Stats
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.launch
import java.time.LocalDate
import java.time.temporal.WeekFields
import java.util.*


class FoodRepository(context: Context) {

    var db: FoodDao = AppDatabase.getInstance(context)?.foodDao()!!

    fun getFood(foodId: Int): Food {
        return db.getFood(foodId)
    }

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

    //Devuelve una lista ordenada del día más viejo al más reciente de la semana
    fun getWeekStats(): List<Stats>{
        var date = LocalDate.now()
        val result: MutableList<Stats> = mutableListOf()
        for (i in 1 .. 7) {
            result.add(getStatsFrom(date))
            date = date.minusDays(1)
        }
        return result.reversed()
    }

    fun getWeekStatsFrom(weekNumber: Int, year: Int): Stats{
        return db.getWeekStatsFrom(weekNumber, year)
    }

    fun getMonthStatsFrom(month: Int, year: Int): Stats{
        return db.getMonthStatsFrom(month, year)
    }

    //Devuelve una lista ordenada de la semana más vieja a la más reciente del mes
    fun getMonthStats(): List<Stats>{
        var date = LocalDate.now()
        var weekNumber: Int
        val result: MutableList<Stats> = mutableListOf()
        for (i in 1 .. 4) {
            weekNumber = date.get(WeekFields.of(Locale.getDefault()).weekOfWeekBasedYear())
            result.add(getWeekStatsFrom(weekNumber, date.year))
            date = date.minusWeeks(1)
        }
        return result.reversed()
    }

    //Devuelve una lista ordenada del mes más viejo al mas reciente de los 6 meses
    fun get6MonthStats(): List<Stats>{
        var date = LocalDate.now()
        val result: MutableList<Stats> = mutableListOf()
        for (i in 1 .. 6) {
            result.add(getMonthStatsFrom(date.monthValue, date.year))
            date = date.minusMonths(1)
        }
        return result.reversed()
    }

    //Devuelve una lista ordenada del mes más viejo al mas reciente del año
    fun getYearStats(): List<Stats>{
        var date = LocalDate.now()
        val result: MutableList<Stats> = mutableListOf()
        for (i in 1 .. 12) {
            result.add(getMonthStatsFrom(date.monthValue, date.year))
            date = date.minusMonths(1)
        }
        return result.reversed()
    }
}