package com.app.kekomi.storage
import android.content.Context
import com.app.kekomi.entities.Food
import com.app.kekomi.entities.Stats
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.launch
import java.util.*


class FoodRepository(context: Context) {

    var db: FoodDao = AppDatabase.getInstance(context)?.foodDao()!!

    //Fetch All the Food
    fun getAllFood(date: Date): List<Food> {
        return db.getAllFood(date)
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

    //
    fun getStatsFrom(date: Date): Stats{
        return db.getStatsFrom(date)
    }
}