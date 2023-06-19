package com.app.kekomi.apis.foodApi

data class FoodResponse(  val parsed: List<ParsedFood>, val text:String)

data class ParsedFood(
    val food: Food
)

data class Food(
    val foodId: String,
    val label: String,
    val knownAs: String,
    val nutrients: Nutrients,
    val category: String,
    val categoryLabel: String,
    val image: String
)

data class Nutrients(
    val ENERC_KCAL: Double,
    val PROCNT: Double,
    val FAT: Double,
    val CHOCDF: Double,
    val FIBTG: Double
)

data class FoodNutrients(
    val calories: String,
    val totalWeight: String,
    val totalNutrients: TotalNutrients,
)
data class TotalNutrients (
    val SUGAR: Nutrient,
    val PROCNT: Nutrient,
    val FAT: Nutrient,
    val NA: Nutrient
)

data class Nutrient (
    val quantity: Double,
    val unit: String
)

data class PostModel(
    val quantity: Int = 1,
    val foodId: String
)
