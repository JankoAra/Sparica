package com.example.sparica.data.models

import android.os.Parcelable
import kotlinx.parcelize.Parcelize
import kotlinx.serialization.Serializable

@Serializable
@Parcelize
enum class Currency(val decimalPlaces: Int) : Parcelable {
    RSD(0),
    EUR(2),
    KRW(0),
    USD(2),
    CZK(0),
    BAM(0),
    QAR(2),
    JPY(0),
    CNY(2),
    AED(2),
    HUF(0)
    ;

    companion object {
        // Map to store the string representation to enum
        private val mapByString: Map<String, Currency> = entries.associateBy { it.name }

        // Function to get the enum by its string representation
        fun fromString(value: String): Currency? {
            return mapByString[value]
        }
    }
}