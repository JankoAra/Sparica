package com.example.sparica.data.models

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.parcelize.Parcelize
import kotlinx.serialization.Contextual
import kotlinx.serialization.Serializable
import java.time.LocalDate

@Serializable
@Parcelize
@Entity(tableName = "exchange_rates")
data class ExchangeRate (
    @PrimaryKey(autoGenerate = true) val id:Int = 0,
    @Contextual val date: LocalDate,
    val baseCurrencyCode:String,
    val targetCurrencyCode:String,
    val exchangeRate: Double
):Parcelable {
    override fun toString(): String {
        return "ExchangeRate(dateTime=$date, baseCurrencyCode='$baseCurrencyCode', targetCurrencyCode='$targetCurrencyCode', exchangeRate=$exchangeRate)"
    }
}