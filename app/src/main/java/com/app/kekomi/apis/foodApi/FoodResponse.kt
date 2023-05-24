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


