package com.app.kekomi.storage

import androidx.room.*
import com.app.kekomi.entities.Food
import java.util.*

@Dao
interface FoodDao {
    @Insert
    fun insertFood(food: Food)

    @Query("Select * from food where date = :date")
    fun getAllFood(date: Date): List<Food>

    @Update
    fun updateFood(food: Food)

    @Delete
    fun deleteFood(food: Food)
}