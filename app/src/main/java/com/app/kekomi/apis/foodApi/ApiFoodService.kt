package com.app.kekomi.apis.foodApi

import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query
import retrofit2.http.Url

interface ApiFoodService {
    @GET
    fun getFoodByName(@Url url:String,
                      @Query ("api_id") api_id:String,
                      @Query ("api_key") api_key:String,
                      @Query ("ingr") foodName:String):Response<FoodResponse>
}