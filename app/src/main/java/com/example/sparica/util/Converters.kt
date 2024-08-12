package com.example.sparica.util

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.room.TypeConverter
import com.example.sparica.data.models.SpendingCategory
import com.example.sparica.data.models.SpendingSubcategory
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.ZoneOffset
import java.time.format.DateTimeFormatter

class Converters {

    @RequiresApi(Build.VERSION_CODES.O)
    @TypeConverter
    fun fromTimestamp(value: Long?): LocalDateTime? {
        return value?.let { LocalDateTime.ofEpochSecond(it, 0, ZoneOffset.UTC) }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    @TypeConverter
    fun dateToTimestamp(date: LocalDateTime?): Long? {
        return date?.toEpochSecond(ZoneOffset.UTC)
    }

    @TypeConverter
    fun fromSpendingCategory(value: SpendingCategory?): String? {
        return value?.let { Json.encodeToString(it) }
    }

    @TypeConverter
    fun toSpendingCategory(value: String?): SpendingCategory? {
        return value?.let { Json.decodeFromString(it) }
    }

    @TypeConverter
    fun fromSpendingSubcategory(value: SpendingSubcategory?): String? {
        return value?.let { Json.encodeToString(it) }
    }

    @TypeConverter
    fun toSpendingSubcategory(value: String?): SpendingSubcategory? {
        return value?.let { Json.decodeFromString(it) }
    }

    private val formatter = DateTimeFormatter.ISO_LOCAL_DATE

    @TypeConverter
    fun fromLocalDate(date: LocalDate?): String? {
        return date?.format(formatter)
    }

    @TypeConverter
    fun toLocalDate(dateString: String?): LocalDate? {
        return dateString?.let { LocalDate.parse(it, formatter) }
    }
}
