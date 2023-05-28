package com.app.kekomi.apis

import com.app.kekomi.apis.foodApi.Nutrient
import com.app.kekomi.apis.foodApi.Nutrients

data class FinalFood (
    val foodId: String,
    val name: String,
    val weight: Int,
    val nutrients: FinalNutrients,
    val image: String,
    val source: Enum<FoodSource>,
)

enum class FoodSource {
    BARCODE,
    FOODAPI
}

data class FinalNutrients(
    val calories: Nutrient,
    val fats: Nutrient,
    val sugar: Nutrient,
    val sodium: Nutrient,
    val protein: Nutrient
)
