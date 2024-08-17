package com.example.sparica.data.api

import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path

interface ExchangeRateService {
    @GET("v6/{apiKey}/latest/USD")
    suspend fun getExchangeRates(@Path("apiKey") apiKey: String): Response<ExchangeRateResponse>
}
