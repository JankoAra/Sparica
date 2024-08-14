package com.example.sparica.data.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.example.sparica.data.models.ExchangeRate
import java.time.LocalDate

@Dao
interface ExchangeRateDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(exchangeRate: ExchangeRate)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(exchangeRates: List<ExchangeRate>)

    @Query("Select * from exchange_rates where date = :date group by date, baseCurrencyCode, targetCurrencyCode, exchangeRate")
    suspend fun getRatesForDate(date: LocalDate): List<ExchangeRate>

    @Query("Select * from exchange_rates where targetCurrencyCode = :targetCode order by date desc limit 1")
    suspend fun getLatestRateForTarget(targetCode: String): ExchangeRate

    @Query(
        "SELECT e.*\n" +
                "FROM exchange_rates e\n" +
                "INNER JOIN (\n" +
                "    SELECT baseCurrencyCode, targetCurrencyCode, MAX(date) as max_date\n" +
                "    FROM exchange_rates\n" +
                "    GROUP BY baseCurrencyCode, targetCurrencyCode\n" +
                ") latest\n" +
                "ON e.baseCurrencyCode = latest.baseCurrencyCode\n" +
                "AND e.targetCurrencyCode = latest.targetCurrencyCode\n" +
                "AND e.date = latest.max_date\n" +
                "group by e.date, e.baseCurrencyCode, e.targetCurrencyCode, e.exchangeRate"
    )
    suspend fun getLatestRates(): List<ExchangeRate>

    @Delete
    suspend fun delete(exchangeRate: ExchangeRate)

    @Update
    suspend fun update(exchangeRate: ExchangeRate)
}