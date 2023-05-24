package com.app.kekomi.apis.foodApi

import retrofit2.Call
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query
import retrofit2.http.Url

public interface ApiFoodService {
    @GET("api/food-database/v2/parser")
    fun getFoodByName(
        @Query("app_id") api_id: String,
        @Query("app_key") api_key: String,
        @Query("ingr") foodName: String
    ): Call<FoodResponse>

    @GET("auto-complete")
    fun autoComplete(
        @Query("app_id") api_id: String,
        @Query("app_key") api_key: String,
        @Query("q") text: String
    ): Call<List<String>>


}