package com.app.kekomi.storage

import androidx.room.*
import com.app.kekomi.entities.Food
import com.app.kekomi.entities.Stats

@Dao
interface FoodDao {
    @Insert
    fun insertFood(food: Food)

    @Query("Select * from food f where day = :day AND month = :month AND year = :year")
    fun getAllFood(day: Int, month: Int, year: Int): List<Food>

    @Query("Select sum(calories*quantity) as calories, " +
            "sum(sodium*quantity) as sodium, " +
            "sum(sugar*quantity) as sugar, " +
            "sum(fats*quantity) as fats, " +
            "sum(protein*quantity) as protein from food where day = :day AND month = :month AND year = :year " +
            "group by day,month,year")
    fun getStatsFrom(day: Int, month: Int, year: Int): Stats

    @Update
    fun updateFood(food: Food)

    @Delete
    fun deleteFood(food: Food)
    @Query("Select sum(calories*quantity) as calories, " +
            "sum(sodium*quantity) as sodium, " +
            "sum(sugar*quantity) as sugar, " +
            "sum(fats*quantity) as fats, " +
            "sum(protein*quantity) as protein from food " +
            "group by day,month,year order by year DESC,month DESC,day DESC limit 7")
    fun getWeekStats(): List<Stats>
    @Query("Select sum(calories*quantity) as calories, " +
            "sum(sodium*quantity) as sodium, " +
            "sum(sugar*quantity) as sugar, " +
            "sum(fats*quantity) as fats, " +
            "sum(protein*quantity) as protein from food " +
            "group by week_number, year order by year DESC, week_number DESC limit 4")
    fun getMonthStats(): List<Stats>
    @Query("Select sum(calories*quantity) as calories, " +
            "sum(sodium*quantity) as sodium, " +
            "sum(sugar*quantity) as sugar, " +
            "sum(fats*quantity) as fats, " +
            "sum(protein*quantity) as protein from food " +
            "group by month,year order by year DESC, month DESC limit 6")
    fun get6MonthStats(): List<Stats>
    @Query("Select sum(calories*quantity) as calories, " +
            "sum(sodium*quantity) as sodium, " +
            "sum(sugar*quantity) as sugar, " +
            "sum(fats*quantity) as fats, " +
            "sum(protein*quantity) as protein from food " +
            "group by month,year order by year DESC, month DESC limit 12")
    fun getYearStats(): List<Stats>

}