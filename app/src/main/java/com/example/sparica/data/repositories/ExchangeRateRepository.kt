package com.example.sparica.data.repositories

import com.example.sparica.data.models.ExchangeRate
import com.example.sparica.data.models.Spending
import java.time.LocalDate

interface ExchangeRateRepository {
    suspend fun insert(exchangeRate: ExchangeRate)

    suspend fun insertAll(exchangeRates: List<ExchangeRate>)

    suspend fun getRatesForDate(date: LocalDate): List<ExchangeRate>

    suspend fun getLatestRateForTarget(targetCode: String): ExchangeRate

    suspend fun getLatestRates():List<ExchangeRate>

    suspend fun delete(exchangeRate: ExchangeRate)

    suspend fun update(exchangeRate: ExchangeRate)

    suspend fun convert(sourceCode:String, targetCode: String, spending: Spending): Spending
}