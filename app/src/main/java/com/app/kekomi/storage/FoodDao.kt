package com.app.kekomi.storage

import androidx.room.*
import com.app.kekomi.entities.Food
import com.app.kekomi.entities.Stats

@Dao
interface FoodDao {
    @Insert
    fun insertFood(food: Food)

    @Query("Select * from food where foodId = :foodId")
    fun getFood(foodId: Int): Food

    @Query("Select * from food f where day = :day AND month = :month AND year = :year")
    fun getAllFood(day: Int, month: Int, year: Int): List<Food>

    @Query("Select sum(calories*quantity) as calories, " +
            "sum(sodium*quantity) as sodium, " +
            "sum(sugar*quantity) as sugar, " +
            "sum(fats*quantity) as fats, " +
            "sum(protein*quantity) as protein from food where day = :day AND month = :month AND year = :year " +
            "group by day,month,year")
    fun getStatsFrom(day: Int, month: Int, year: Int): Stats

    @Query("Select sum(calories*quantity) as calories, " +
            "sum(sodium*quantity) as sodium, " +
            "sum(sugar*quantity) as sugar, " +
            "sum(fats*quantity) as fats, " +
            "sum(protein*quantity) as protein from food " +
            "where week_number = :weekNumber and year = :year " +
            "group by week_number")
    fun getWeekStatsFrom(weekNumber: Int, year: Int): Stats

    @Query("Select sum(calories*quantity) as calories, " +
            "sum(sodium*quantity) as sodium, " +
            "sum(sugar*quantity) as sugar, " +
            "sum(fats*quantity) as fats, " +
            "sum(protein*quantity) as protein from food " +
            "where month = :month and year = :year " +
            "group by month")
    fun getMonthStatsFrom(month: Int, year: Int): Stats

    @Update
    fun updateFood(food: Food)

    @Delete
    fun deleteFood(food: Food)
}