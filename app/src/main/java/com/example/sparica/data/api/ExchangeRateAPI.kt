package com.example.sparica.data.api

import android.util.Log
import com.example.sparica.BuildConfig
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object ExchangeRateAPI {
    private val apiKey = BuildConfig.EXCHANGE_RATE_API_KEY

    suspend fun fetchExchangeRates(apiKey: String = this.apiKey): ExchangeRateResponse? {
        val retrofit = Retrofit.Builder()
            .baseUrl("https://v6.exchangerate-api.com/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
        Log.d("API Service", "retrofit created")

        val service = retrofit.create(ExchangeRateService::class.java)
        Log.d("API Service", "service created")

        return try {
            val response: Response<ExchangeRateResponse> = service.getExchangeRates(apiKey)
            Log.d("API Service", "call executed")
            if (response.isSuccessful) {
                response.body()
            } else {
                Log.e("API Service", "Error: ${response.errorBody()?.string()}")
                null
            }
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
    }
}