package com.app.kekomi.storage

import androidx.room.*
import com.app.kekomi.entities.Food
import com.app.kekomi.entities.Stats
import java.util.*

@Dao
interface FoodDao {
    @Insert
    fun insertFood(food: Food)

    @Query("Select * from food where date = :date")
    fun getAllFood(date: Date): List<Food>

    @Query("Select sum(calories*quantity) as calories, " +
            "sum(sodium*quantity) as sodium, " +
            "sum(sugar*quantity) as sugar, " +
            "sum(fats*quantity) as fats, " +
            "sum(protein*quantity) as protein from food where date = :date  group by date")
    fun getStatsFrom(date: Date): Stats


    @Update
    fun updateFood(food: Food)

    @Delete
    fun deleteFood(food: Food)
}