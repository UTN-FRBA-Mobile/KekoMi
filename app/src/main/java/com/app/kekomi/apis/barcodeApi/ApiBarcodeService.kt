package com.app.kekomi.apis.barcodeApi

import androidx.core.view.accessibility.AccessibilityEventCompat.ContentChangeType
import com.app.kekomi.apis.foodApi.FoodNutrients
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.Response
import retrofit2.http.*

public interface ApiBarcodeService {

    @GET("food/")
    fun getFoodNutrients(
        @Query("barcode") barcode: String
    ): Call<List<BarcodeFood>>


}