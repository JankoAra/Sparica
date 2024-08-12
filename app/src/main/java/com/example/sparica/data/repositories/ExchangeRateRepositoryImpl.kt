package com.example.sparica.data.repositories

import android.util.Log
import com.example.sparica.data.api.ExchangeRateAPI
import com.example.sparica.data.api.ExchangeRateResponse
import com.example.sparica.data.dao.ExchangeRateDao
import com.example.sparica.data.models.Currency
import com.example.sparica.data.models.ExchangeRate
import com.example.sparica.data.models.Spending
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.time.LocalDate

class ExchangeRateRepositoryImpl(
    private val exchangeRateDao: ExchangeRateDao,
    private val exchangeRateApi: ExchangeRateAPI
) : ExchangeRateRepository {

    override suspend fun insert(exchangeRate: ExchangeRate) {
        exchangeRateDao.insert(exchangeRate)
    }

    override suspend fun insertAll(exchangeRates: List<ExchangeRate>) {
        exchangeRateDao.insertAll(exchangeRates)
    }

    override suspend fun getRatesForDate(date: LocalDate): List<ExchangeRate> {
        if (date.equals(LocalDate.now())) return refreshRatesIfNeeded()
        return exchangeRateDao.getRatesForDate(date)
    }

    override suspend fun getLatestRateForTarget(targetCode: String): ExchangeRate {
        return refreshRatesIfNeeded().filter { it.targetCurrencyCode == targetCode }.first()
    }

    override suspend fun getLatestRates(): List<ExchangeRate> {
        return refreshRatesIfNeeded()
    }

    override suspend fun delete(exchangeRate: ExchangeRate) {
        exchangeRateDao.delete(exchangeRate)
    }

    override suspend fun update(exchangeRate: ExchangeRate) {
        exchangeRateDao.update(exchangeRate)
    }

    override suspend fun convert(sourceCode:String, targetCode: String, spending:Spending):Spending{
        val sourceRate = getLatestRateForTarget(sourceCode).exchangeRate
        val targetRate = getLatestRateForTarget(targetCode).exchangeRate
        val convertedPrice = spending.price/sourceRate*targetRate
        return spending.copy(currency = Currency.fromString(targetCode)!!, price = convertedPrice)
    }

    private suspend fun refreshRatesIfNeeded(): List<ExchangeRate> {
        val today = LocalDate.now()

        // Check if we already have today's rates
        val existingRates = exchangeRateDao.getRatesForDate(today)
        if (existingRates.isNotEmpty()) {
            Log.d("API call", "Latest rates are already fetched")
            return existingRates
        } else {
            // Fetch the latest rates from the API

            val apiResponse: ExchangeRateResponse? = withContext(Dispatchers.IO) {
                Log.d("API call", "API fetch latest rates call started")
                exchangeRateApi.fetchExchangeRates()
            }
            Log.d("API call", "API fetch latest rates call finished")
            if (apiResponse?.result.equals("success")) {
                // Parse and insert the data into the database
                val newRates =
                    apiResponse?.conversionRates?.map { (targetCurrencyCode, exchangeRate) ->
                        ExchangeRate(
                            date = today,
                            baseCurrencyCode = apiResponse.baseCode,
                            targetCurrencyCode = targetCurrencyCode,
                            exchangeRate = exchangeRate
                        )
                    }
                if (newRates != null) {
                    exchangeRateDao.insertAll(newRates)
                }
                Log.d("API call", "API fetch latest rates succeded")
            } else {
                Log.d("API call", "API fetch latest rates failed: ${apiResponse?.errorType}")
            }
            return exchangeRateDao.getLatestRates()
        }
    }
}