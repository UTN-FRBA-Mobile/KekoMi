package com.app.kekomi.entities
import androidx.room.Entity
import androidx.room.PrimaryKey



@Entity(tableName = "food")
data class Food(
    @PrimaryKey(autoGenerate = true)
    var foodId: Int? = null,
    var foodName: String,
    var calories: Int,
    var sodium: Int,
    var sugar: Int,
    var fats: Int,
    var protein: Int,
    var quantity: Int,
    var meal: Meal,
    var day: Int,
    var week_number: Int,
    var month: Int,
    var year: Int
    )
