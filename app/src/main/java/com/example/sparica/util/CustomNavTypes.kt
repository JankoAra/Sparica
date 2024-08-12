package com.example.sparica.util

import android.os.Build
import android.os.Bundle
import androidx.navigation.NavType
import com.example.sparica.data.models.Spending
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import kotlinx.serialization.modules.SerializersModule
import kotlinx.serialization.modules.contextual

object CustomNavTypes {
    val serializationModule = SerializersModule {
        contextual(LocalDateTimeSerializer)
        // Add any other custom serializers here
    }
    val json = Json {
        serializersModule = serializationModule
    }

    val SpendingType = object : NavType<Spending>(false) {
        override fun get(bundle: Bundle, key: String): Spending? {
            return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                bundle.getParcelable(key, Spending::class.java)
            } else {
                bundle.getParcelable(key)
            }
        }

        override fun parseValue(value: String): Spending {
            return json.decodeFromString(value)
        }

        override fun put(bundle: Bundle, key: String, value: Spending) {
            bundle.putParcelable(key, value)
        }

        override fun serializeAsValue(value: Spending): String {
            return json.encodeToString(value)
        }

    }
}