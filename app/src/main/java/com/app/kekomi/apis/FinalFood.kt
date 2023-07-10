package com.app.kekomi.apis

import com.app.kekomi.apis.foodApi.Nutrient
import com.app.kekomi.apis.foodApi.Nutrients

data class FinalFood (
    val foodId: String,
    val name: String,
    val weight: Int,
    val nutrients: FinalNutrients,
    val source: Enum<FoodSource>
)

enum class FoodSource {
    BARCODE,
    FOODAPI
}

data class FinalNutrients(
    val calories: Nutrient?= Nutrient(0.0, ""),
    val fats: Nutrient?= Nutrient(0.0, ""),
    val sugar: Nutrient?= Nutrient(0.0, ""),
    val sodium: Nutrient?= Nutrient(0.0, ""),
    val protein: Nutrient?= Nutrient(0.0, "")
)
