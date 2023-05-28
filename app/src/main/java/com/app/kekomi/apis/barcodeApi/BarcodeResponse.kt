package com.app.kekomi.apis.barcodeApi

import android.util.Log
import com.app.kekomi.Views.api_id
import com.app.kekomi.Views.api_key
import com.app.kekomi.apis.FinalNutrients
import com.app.kekomi.apis.foodApi.ApiFoodService
import com.app.kekomi.apis.foodApi.Food
import com.app.kekomi.apis.foodApi.FoodResponse
import com.google.gson.GsonBuilder
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

data class BarcodeResponse(
    val response: List<BarcodeFood>
)

data class BarcodeFood(
    val id: Int,
    val barcode: String,
    val food: String,
    val weight: Int,
    val nutrients: FinalNutrients
)


private fun getRetrofit(): Retrofit {
    val gson = GsonBuilder()
        .setLenient()
        .create()

    return Retrofit.Builder()
        .baseUrl("https://6473b286d784bccb4a3cda0e.mockapi.io/barcode/")
        .addConverterFactory(GsonConverterFactory.create(gson))
        .build()
}


fun getFoodByBarcode(barcode: String, callback: (List<BarcodeFood>) -> Unit) {
    val apiService = getRetrofit().create(ApiBarcodeService::class.java)
    val call: Call<List<BarcodeFood>> = apiService.getFoodNutrients(barcode)

    call.enqueue(object : Callback<List<BarcodeFood>> {
        override fun onResponse(call: Call<List<BarcodeFood>>, response: Response<List<BarcodeFood>>) {
            if (response.isSuccessful) {
                val foodResponse: List<BarcodeFood>? = response.body()
                if (foodResponse != null) {
                    Log.d("Main", "Success! $foodResponse")
                    callback(foodResponse)
                } else {
                    Log.e("Main", "Empty response body")
                }
            } else {
                Log.e("Main", "Request failed with code: ${response.code()}")
            }
        }

        override fun onFailure(call: Call<List<BarcodeFood>>, t: Throwable) {
            Log.e("Main", "Request failed: ${t.message}")
        }
    })
}

