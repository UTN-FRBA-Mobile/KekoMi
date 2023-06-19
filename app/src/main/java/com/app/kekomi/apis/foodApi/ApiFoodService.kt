package com.app.kekomi.apis.foodApi

import androidx.core.view.accessibility.AccessibilityEventCompat.ContentChangeType
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.Response
import retrofit2.http.*

public interface ApiFoodService {
    @GET("api/food-database/v2/parser")
    fun getFoodByName(
        @Query("app_id") api_id: String,
        @Query("app_key") api_key: String,
        @Query("ingr") foodName: String
    ): Call<FoodResponse>

    @Headers(
        "Content-Type:application/json"
    )
    @POST("api/food-database/v2/nutrients")
    fun getFoodNutrients(
        @Query("app_id") api_id: String,
        @Query("app_key") api_key: String,
        @Body body: RequestBody
    ): Call<FoodNutrients>

    @GET("auto-complete")
    fun autoComplete(
        @Query("app_id") api_id: String,
        @Query("app_key") api_key: String,
        @Query("q") text: String
    ): Call<List<String>>


}